#!/usr/bin/env python3
"""Start CI test containers with bounded pull retries and readiness checks."""

from __future__ import annotations

import argparse
import json
import random
import re
import subprocess
import time
import urllib.error
import urllib.request
from collections.abc import Callable, Sequence
from dataclasses import dataclass
from typing import Final

WEBSITE_IMAGE: Final = (
    "gprestes/the-internet:v2.6.5"
    "@sha256:205b8fc712747ea5fa1a0d54b01fa02d65e3f67a448c7a5c2aba65b211def171"
)
SELENIUM_HUB_IMAGE: Final = (
    "selenium/hub:4.45.0-20260606"
    "@sha256:684d163880d558f217b29d24e992c5a52e0b130381ab7e1f79e9bad0414f753b"
)
NETWORK_NAME: Final = "the-internet-tests-ci"
WEBSITE_CONTAINER: Final = "the-internet-tests-ci-website"
GRID_HUB_CONTAINER: Final = "the-internet-tests-ci-grid-hub"
GRID_NODE_CONTAINER: Final = "the-internet-tests-ci-grid-node"
CONTAINER_NAMES: Final = (WEBSITE_CONTAINER, GRID_NODE_CONTAINER, GRID_HUB_CONTAINER)
IMAGE_REFERENCE_PATTERN: Final = re.compile(r"^[A-Za-z0-9][A-Za-z0-9._/@:+-]{0,511}$")


class ContainerStartupError(RuntimeError):
    """Raised when a required CI container cannot be pulled or started."""


@dataclass(frozen=True, slots=True)
class RetryPolicy:
    """Bounds retries for an idempotent image pull."""

    attempts: int = 4
    timeout_seconds: float = 90.0
    base_delay_seconds: float = 5.0
    max_delay_seconds: float = 30.0

    def __post_init__(self) -> None:
        """Validate retry bounds."""
        if self.attempts < 1:
            raise ValueError("attempts must be at least 1")
        for name, value in (
            ("timeout_seconds", self.timeout_seconds),
            ("base_delay_seconds", self.base_delay_seconds),
            ("max_delay_seconds", self.max_delay_seconds),
        ):
            if value <= 0:
                raise ValueError(f"{name} must be greater than 0")


type CommandRunner = Callable[
    [Sequence[str], float | None, bool, bool], subprocess.CompletedProcess[str]
]


def run_command(
    command: Sequence[str],
    timeout_seconds: float | None = None,
    check: bool = True,
    quiet: bool = False,
) -> subprocess.CompletedProcess[str]:
    """Run a command without invoking a shell."""
    return subprocess.run(
        list(command),
        check=check,
        stderr=subprocess.DEVNULL if quiet else None,
        stdout=subprocess.DEVNULL if quiet else None,
        text=True,
        timeout=timeout_seconds,
    )


def validate_image_reference(image: str) -> str:
    """Validate an image reference received from workflow matrix data."""
    if IMAGE_REFERENCE_PATTERN.fullmatch(image) is None:
        raise ValueError(
            "image reference must contain 1 to 512 Docker-reference characters "
            "and cannot begin with an option prefix"
        )
    return image


def pull_image(
    image: str,
    *,
    policy: RetryPolicy,
    runner: CommandRunner = run_command,
    sleeper: Callable[[float], None] = time.sleep,
    randomizer: Callable[[float, float], float] = random.uniform,
) -> None:
    """Pull an image with bounded exponential backoff and full jitter."""
    validated_image = validate_image_reference(image)
    last_failure: BaseException | None = None

    for attempt in range(1, policy.attempts + 1):
        print(
            f"Pulling {validated_image} (attempt {attempt}/{policy.attempts})",
            flush=True,
        )
        try:
            runner(
                ("docker", "pull", validated_image),
                policy.timeout_seconds,
                True,
                False,
            )
            return
        except (subprocess.CalledProcessError, subprocess.TimeoutExpired) as failure:
            last_failure = failure
            if attempt == policy.attempts:
                break
            delay_cap = min(
                policy.max_delay_seconds,
                policy.base_delay_seconds * (2 ** (attempt - 1)),
            )
            delay = randomizer(0.0, delay_cap)
            print(
                f"::warning title=Container pull retry::{validated_image} failed "
                f"on attempt {attempt}; retrying in {delay:.1f}s",
                flush=True,
            )
            sleeper(delay)

    raise ContainerStartupError(
        f"Failed to pull {validated_image} after {policy.attempts} attempts"
    ) from last_failure


def pull_images(
    images: Sequence[str],
    *,
    policy: RetryPolicy,
    initial_jitter_seconds: float = 0.0,
    runner: CommandRunner = run_command,
    sleeper: Callable[[float], None] = time.sleep,
    randomizer: Callable[[float, float], float] = random.uniform,
) -> None:
    """Pull unique images after an optional anti-stampede delay."""
    if initial_jitter_seconds < 0 or initial_jitter_seconds > 120:
        raise ValueError("initial_jitter_seconds must be between 0 and 120")
    if initial_jitter_seconds:
        delay = randomizer(0.0, initial_jitter_seconds)
        print(f"Staggering container pulls by {delay:.1f}s", flush=True)
        sleeper(delay)

    for image in dict.fromkeys(images):
        pull_image(
            image,
            policy=policy,
            runner=runner,
            sleeper=sleeper,
            randomizer=randomizer,
        )


def _fetch_json(url: str, timeout_seconds: float) -> object:
    """Fetch and decode a JSON readiness response."""
    with urllib.request.urlopen(url, timeout=timeout_seconds) as response:
        return json.load(response)


def wait_for_readiness(
    description: str,
    probe: Callable[[], bool],
    *,
    timeout_seconds: float = 90.0,
    interval_seconds: float = 2.0,
    monotonic: Callable[[], float] = time.monotonic,
    sleeper: Callable[[float], None] = time.sleep,
) -> None:
    """Poll a service until it is ready or the bounded deadline expires."""
    deadline = monotonic() + timeout_seconds
    last_failure: BaseException | None = None
    while monotonic() < deadline:
        try:
            if probe():
                print(f"{description} is ready", flush=True)
                return
        except (OSError, ValueError, urllib.error.URLError) as failure:
            last_failure = failure
        sleeper(interval_seconds)

    message = f"{description} was not ready within {timeout_seconds:.0f}s"
    if last_failure is not None:
        message = f"{message}: {last_failure}"
    raise ContainerStartupError(message)


def website_is_ready() -> bool:
    """Return whether the test application responds successfully."""
    with urllib.request.urlopen("http://localhost:7080/", timeout=5) as response:
        status = response.status
        if not isinstance(status, int):
            raise ContainerStartupError(
                "Test application returned an invalid HTTP status"
            )
        return 200 <= status < 400


def grid_is_ready() -> bool:
    """Return whether Selenium Grid reports a registered, ready node."""
    payload = _fetch_json("http://localhost:4444/status", 5)
    if not isinstance(payload, dict):
        return False
    value = payload.get("value")
    return isinstance(value, dict) and value.get("ready") is True


def _ensure_network(runner: CommandRunner) -> None:
    inspected = runner(
        ("docker", "network", "inspect", NETWORK_NAME), 15.0, False, True
    )
    if inspected.returncode != 0:
        runner(("docker", "network", "create", NETWORK_NAME), 30.0, True, False)


def _remove_resources(runner: CommandRunner) -> None:
    for container_name in CONTAINER_NAMES:
        runner(("docker", "rm", "--force", container_name), 30.0, False, True)
    runner(("docker", "network", "rm", NETWORK_NAME), 30.0, False, True)


def _print_diagnostics(runner: CommandRunner) -> None:
    print("::group::CI container diagnostics", flush=True)
    runner(("docker", "ps", "--all"), 30.0, False, False)
    for container_name in CONTAINER_NAMES:
        runner(("docker", "logs", "--tail", "200", container_name), 30.0, False, False)
    print("::endgroup::", flush=True)


def _start_website(runner: CommandRunner) -> None:
    runner(
        (
            "docker",
            "run",
            "--detach",
            "--name",
            WEBSITE_CONTAINER,
            "--network",
            NETWORK_NAME,
            "--network-alias",
            "website",
            "--publish",
            "7080:5000",
            "--pull",
            "never",
            WEBSITE_IMAGE,
        ),
        60.0,
        True,
        False,
    )


def _start_grid(node_image: str, runner: CommandRunner) -> None:
    runner(
        (
            "docker",
            "run",
            "--detach",
            "--name",
            GRID_HUB_CONTAINER,
            "--network",
            NETWORK_NAME,
            "--network-alias",
            "selenium-hub",
            "--publish",
            "4442:4442",
            "--publish",
            "4443:4443",
            "--publish",
            "4444:4444",
            "--pull",
            "never",
            SELENIUM_HUB_IMAGE,
        ),
        60.0,
        True,
        False,
    )
    runner(
        (
            "docker",
            "run",
            "--detach",
            "--name",
            GRID_NODE_CONTAINER,
            "--network",
            NETWORK_NAME,
            "--shm-size",
            "2g",
            "--env",
            "SE_EVENT_BUS_HOST=selenium-hub",
            "--env",
            "SE_EVENT_BUS_PUBLISH_PORT=4442",
            "--env",
            "SE_EVENT_BUS_SUBSCRIBE_PORT=4443",
            "--env",
            "SE_NODE_ENABLE_MANAGED_DOWNLOADS=true",
            "--env",
            "SE_NODE_MAX_SESSIONS=1",
            "--pull",
            "never",
            node_image,
        ),
        60.0,
        True,
        False,
    )


def start_services(
    *,
    grid_node_image: str | None,
    policy: RetryPolicy,
    initial_jitter_seconds: float,
    runner: CommandRunner = run_command,
) -> None:
    """Pull, start, and verify the application and optional Selenium Grid."""
    images = [WEBSITE_IMAGE]
    validated_node_image: str | None = None
    if grid_node_image is not None:
        validated_node_image = validate_image_reference(grid_node_image)
        images.extend((SELENIUM_HUB_IMAGE, validated_node_image))

    pull_images(
        images,
        policy=policy,
        initial_jitter_seconds=initial_jitter_seconds,
        runner=runner,
    )
    _remove_resources(runner)

    try:
        _ensure_network(runner)
        _start_website(runner)
        if validated_node_image is not None:
            _start_grid(validated_node_image, runner)
        wait_for_readiness("Test application", website_is_ready)
        if validated_node_image is not None:
            wait_for_readiness("Selenium Grid", grid_is_ready)
    except BaseException:
        _print_diagnostics(runner)
        _remove_resources(runner)
        raise


def _retry_policy_from_args(args: argparse.Namespace) -> RetryPolicy:
    return RetryPolicy(
        attempts=args.pull_attempts,
        timeout_seconds=args.pull_timeout_seconds,
    )


def build_parser() -> argparse.ArgumentParser:
    """Build the command-line parser."""
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--pull-attempts", type=int, default=4)
    parser.add_argument("--pull-timeout-seconds", type=float, default=90.0)
    parser.add_argument("--initial-jitter-seconds", type=float, default=0.0)
    subparsers = parser.add_subparsers(dest="command", required=True)

    pull_parser = subparsers.add_parser("pull", help="Pull images with retries")
    pull_parser.add_argument("images", nargs="+")

    subparsers.add_parser("start-website", help="Start the test application")
    grid_parser = subparsers.add_parser("start-grid", help="Start the app and Grid")
    grid_parser.add_argument("--node-image", required=True)
    subparsers.add_parser("cleanup", help="Remove CI containers and network")
    return parser


def main(argv: Sequence[str] | None = None) -> int:
    """Run the requested CI container operation."""
    args = build_parser().parse_args(argv)
    policy = _retry_policy_from_args(args)

    if args.command == "pull":
        pull_images(
            args.images,
            policy=policy,
            initial_jitter_seconds=args.initial_jitter_seconds,
        )
    elif args.command == "start-website":
        start_services(
            grid_node_image=None,
            policy=policy,
            initial_jitter_seconds=args.initial_jitter_seconds,
        )
    elif args.command == "start-grid":
        start_services(
            grid_node_image=args.node_image,
            policy=policy,
            initial_jitter_seconds=args.initial_jitter_seconds,
        )
    else:
        _remove_resources(run_command)
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
