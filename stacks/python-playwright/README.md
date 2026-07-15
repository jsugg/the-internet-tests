# Python Playwright stack

Pytest + Playwright coverage for the shared scenario catalog. This stack covers
the Python P0 UI suite, selected P1 data-modeling coverage, and Chromium-only HTTP/resource checks.

## Prerequisites

- Python 3.14 or newer
- pip
- Docker, when running against the local The Internet app container
- Docker Compose

## Install

Run the pip, Playwright, and pytest commands in this guide from this stack
directory, so that the virtual environment and `pyproject.toml` resolve. Docker
Compose commands are the exception and run from the repository root, as each
step below states.

```bash
cd stacks/python-playwright
python3 -m venv .venv
source .venv/bin/activate
python -m pip install --upgrade pip
python -m pip install -e ".[dev]"
python -m playwright install --with-deps chromium
```

## Run locally

Start the demo app from the repository root:

```bash
docker compose -f docker/compose.yml up -d website
```

Run the smoke scenario from this stack directory:

```bash
THE_INTERNET_BASE_URL=http://localhost:7080 pytest -m smoke --browser chromium
```

Run the HTTP/resource slice:

```bash
THE_INTERNET_BASE_URL=http://localhost:7080 pytest -m http --browser chromium
```

Run static checks:

```bash
ruff check .
ruff format --check .
mypy src tests
```

Stop the demo app from the repository root when finished:

```bash
docker compose -f docker/compose.yml down
```

## Browser projects

The pytest-playwright plugin supplies Chromium, Firefox, WebKit, branded browser
channels, and device emulation through CLI flags. CI keeps the Python gate
definition aligned with the TypeScript Playwright stack: Chromium smoke in the
fast PR gate, Chromium/Firefox/WebKit plus mobile-emulation slices in the
path-filtered full gate, and branded Chrome/Edge plus full mobile slices nightly.

## Current selected P1 coverage

- Sortable table extraction and column-order assertions

## Reports

Local runs write Playwright failure artifacts to `test-results/`. CI stages the
same outputs under `artifacts/py/<run-id>/<slice>/test-results/` and writes
pytest JUnit XML beside them.
