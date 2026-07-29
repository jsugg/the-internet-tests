"""Tests for the bounded CI container bootstrap helper."""

from __future__ import annotations

import subprocess
import unittest
from collections.abc import Sequence

from tools import ci_containers


class RecordingRunner:
    """Record commands and return configured results."""

    def __init__(self, failures: int = 0) -> None:
        self.commands: list[tuple[str, ...]] = []
        self.failures = failures

    def __call__(
        self,
        command: Sequence[str],
        timeout_seconds: float | None,
        check: bool,
        quiet: bool,
    ) -> subprocess.CompletedProcess[str]:
        del timeout_seconds, check, quiet
        recorded = tuple(command)
        self.commands.append(recorded)
        if self.failures:
            self.failures -= 1
            raise subprocess.CalledProcessError(1, recorded)
        return subprocess.CompletedProcess(recorded, 0)


class RetryPolicyTest(unittest.TestCase):
    """Validate retry-policy contracts."""

    def test_rejects_invalid_attempt_count(self) -> None:
        with self.assertRaisesRegex(ValueError, "attempts"):
            ci_containers.RetryPolicy(attempts=0)

    def test_rejects_non_positive_timeout(self) -> None:
        with self.assertRaisesRegex(ValueError, "timeout_seconds"):
            ci_containers.RetryPolicy(timeout_seconds=0)


class ImageReferenceTest(unittest.TestCase):
    """Validate untrusted matrix image references."""

    def test_accepts_digest_pinned_reference(self) -> None:
        image = "registry.example/image:v1@sha256:" + ("a" * 64)
        self.assertEqual(ci_containers.validate_image_reference(image), image)

    def test_rejects_whitespace(self) -> None:
        with self.assertRaisesRegex(ValueError, "Docker-reference"):
            ci_containers.validate_image_reference("image:latest\n--privileged")

    def test_rejects_empty_reference(self) -> None:
        with self.assertRaisesRegex(ValueError, "1 to 512"):
            ci_containers.validate_image_reference("")

    def test_rejects_option_prefix(self) -> None:
        with self.assertRaisesRegex(ValueError, "option prefix"):
            ci_containers.validate_image_reference("--platform=host")


class PullImageTest(unittest.TestCase):
    """Verify bounded retry behavior."""

    def test_retries_then_succeeds(self) -> None:
        runner = RecordingRunner(failures=2)
        delays: list[float] = []

        ci_containers.pull_image(
            "example/image:v1",
            policy=ci_containers.RetryPolicy(attempts=3),
            runner=runner,
            sleeper=delays.append,
            randomizer=lambda _minimum, maximum: maximum,
        )

        self.assertEqual(len(runner.commands), 3)
        self.assertEqual(delays, [5.0, 10.0])

    def test_surfaces_exhausted_retries(self) -> None:
        runner = RecordingRunner(failures=2)

        with self.assertRaisesRegex(
            ci_containers.ContainerStartupError, "after 2 attempts"
        ):
            ci_containers.pull_image(
                "example/image:v1",
                policy=ci_containers.RetryPolicy(attempts=2),
                runner=runner,
                sleeper=lambda _delay: None,
            )

        self.assertEqual(len(runner.commands), 2)

    def test_deduplicates_images(self) -> None:
        runner = RecordingRunner()

        ci_containers.pull_images(
            ("example/image:v1", "example/image:v1"),
            policy=ci_containers.RetryPolicy(),
            runner=runner,
        )

        self.assertEqual(runner.commands, [("docker", "pull", "example/image:v1")])

    def test_applies_bounded_initial_jitter(self) -> None:
        runner = RecordingRunner()
        delays: list[float] = []

        ci_containers.pull_images(
            ("example/image:v1",),
            policy=ci_containers.RetryPolicy(),
            initial_jitter_seconds=20,
            runner=runner,
            sleeper=delays.append,
            randomizer=lambda _minimum, maximum: maximum / 2,
        )

        self.assertEqual(delays, [10.0])


class ReadinessTest(unittest.TestCase):
    """Verify readiness polling contracts."""

    def test_retries_until_ready(self) -> None:
        outcomes = iter((False, False, True))
        clock = iter((0.0, 0.0, 1.0, 2.0))
        delays: list[float] = []

        ci_containers.wait_for_readiness(
            "service",
            lambda: next(outcomes),
            timeout_seconds=10,
            interval_seconds=1,
            monotonic=lambda: next(clock),
            sleeper=delays.append,
        )

        self.assertEqual(delays, [1, 1])

    def test_times_out_with_actionable_error(self) -> None:
        clock = iter((0.0, 0.0, 2.0))

        with self.assertRaisesRegex(ci_containers.ContainerStartupError, "not ready"):
            ci_containers.wait_for_readiness(
                "service",
                lambda: False,
                timeout_seconds=1,
                monotonic=lambda: next(clock),
                sleeper=lambda _delay: None,
            )


if __name__ == "__main__":
    unittest.main()
