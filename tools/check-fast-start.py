#!/usr/bin/env python3
"""Statically validate the root README fast-start block.

The fast start is the first thing a new contributor runs, so a stale command
there is the most expensive kind of documentation rot. This walks the block and
checks that everything it names still exists: the compose file parses, the
directories are real, and the npm scripts are defined.

It never starts a browser, installs anything, or runs a test. It only checks
that the commands refer to things that exist.
"""
from __future__ import annotations

import json, os, re, shutil, subprocess, sys
from pathlib import Path

README = Path("README.md")
SECTION = "## 3. Fast start"
ENV_PREFIX = re.compile(r"^(?:[A-Z_][A-Z0-9_]*=\S*\s+)+")

def fast_start_commands() -> list[str]:
    lines = README.read_text().splitlines()
    if SECTION not in lines:
        sys.exit(f"{README}: section '{SECTION}' not found; the fast-start check is anchored to it")
    out: list[str] = []
    fenced = False
    for line in lines[lines.index(SECTION) + 1:]:
        if line.startswith("## "):
            break
        if line.startswith("```"):
            fenced = line.strip() == "```bash"
            continue
        if fenced and line.strip():
            out.append(line.strip())
    if not out:
        sys.exit(f"{README}: no ```bash block under '{SECTION}'")
    return out

def compose_file(cwd: Path, cmd: str, errors: list[str]) -> None:
    m = re.search(r"-f\s+(\S+)", cmd)
    if not m:
        errors.append(f"docker compose without -f, cannot verify: {cmd}")
        return
    path = Path(os.path.normpath(cwd / m.group(1)))
    if not path.is_file():
        errors.append(f"compose file referenced by the fast start does not exist: {path}")
        return
    service = cmd.split()[-1] if cmd.rstrip().endswith(("website",)) else None
    if service and f"{service}:" not in path.read_text():
        errors.append(f"compose service '{service}' not defined in {path}")
    if shutil.which("docker") is None:
        print(f"  skip  {path} parse check (docker CLI not available)")
        return
    proc = subprocess.run(["docker", "compose", "-f", str(path), "config", "-q"],
                          capture_output=True, text=True)
    if proc.returncode != 0:
        errors.append(f"{path} does not parse: {proc.stderr.strip()}")
    else:
        print(f"  ok    {path} parses")

def npm_script(cwd: Path, script: str, errors: list[str]) -> None:
    pkg = cwd / "package.json"
    if not pkg.is_file():
        errors.append(f"'npm run {script}' has no package.json in {cwd}")
        return
    scripts = json.loads(pkg.read_text()).get("scripts", {})
    if script not in scripts:
        errors.append(f"npm script '{script}' is not defined in {pkg} (fast start would fail)")
    else:
        print(f"  ok    npm run {script} -> {scripts[script]}")

def main() -> None:
    errors: list[str] = []
    cwd = Path(".")
    for raw in fast_start_commands():
        cmd = ENV_PREFIX.sub("", raw)
        if cmd.startswith("cd "):
            cwd = Path(os.path.normpath(cwd / cmd[3:].strip()))
            if not cwd.is_dir():
                errors.append(f"fast start does 'cd' into a directory that does not exist: {cwd}")
            else:
                print(f"  ok    cd {cwd}")
        elif cmd.startswith("docker compose"):
            compose_file(cwd, cmd, errors)
        elif cmd.startswith("npm run "):
            npm_script(cwd, cmd.split()[2], errors)
        elif cmd.startswith("npm ci"):
            for f in ("package.json", "package-lock.json"):
                if not (cwd / f).is_file():
                    errors.append(f"'npm ci' in {cwd} requires {f}, which does not exist")
            print(f"  ok    npm ci in {cwd}")
        elif cmd.startswith("npx playwright install"):
            pkg = cwd / "package.json"
            if pkg.is_file() and "playwright" not in pkg.read_text():
                errors.append(f"'npx playwright install' in {cwd}, but playwright is not a dependency there")
            print(f"  ok    npx playwright install in {cwd}")
        else:
            errors.append(f"fast-start command not understood by this check: {cmd!r}")
    if errors:
        sys.exit("README fast start is stale:\n" + "\n".join(f"  - {e}" for e in errors))
    print("readme fast start ok: every command refers to something that exists")

main()
