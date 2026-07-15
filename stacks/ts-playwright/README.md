# TypeScript Playwright stack

Playwright coverage for the shared scenario catalog. This stack covers the TypeScript P0 UI suite and Chromium-only HTTP/resource checks.

## Prerequisites

- Node.js 22.13 or newer
- npm
- Docker, when running against the local The Internet app container
- Docker Compose

## Install

Run the npm and Playwright commands in this guide from this stack directory. Docker Compose commands are the exception and run from the repository root, as each step below states.

```bash
cd stacks/ts-playwright
npm ci
npx playwright install --with-deps chromium
```

## Run locally

Start the demo app from the repository root:

```bash
docker compose -f docker/compose.yml up -d website
```

Run the smoke scenario from this stack directory:

```bash
THE_INTERNET_BASE_URL=http://localhost:7080 npm run test:chromium:smoke
```

Run the HTTP/resource slice:

```bash
THE_INTERNET_BASE_URL=http://localhost:7080 npm run test:chromium:http
```

Run the flake lab deliberately; it is excluded from normal gates:

```bash
THE_INTERNET_BASE_URL=http://localhost:7080 npm run test:flake-lab
```

See [`../../docs/flakiness-guide.md`](../../docs/flakiness-guide.md) before adding or retagging flake-lab scenarios.

Run static checks:

```bash
npm run typecheck
npm run lint
npm run format:check
```

Stop the demo app from the repository root when finished:

```bash
docker compose -f docker/compose.yml down
```

## Browser projects

`playwright.config.ts` defines desktop Chromium, Firefox, WebKit, branded Chrome and Edge channels, Mobile Chrome, and Mobile Safari projects. PR CI runs only the Chromium smoke project to keep feedback fast. The TypeScript full gate runs `@http` tests on Chromium only because HTTP/resource checks do not exercise rendering engines.

## Reports

Local runs write the HTML report to `playwright-report/` and failure artifacts to `test-results/`. CI writes the same outputs under `artifacts/ts/<run-id>/<slice>/`: HTML reports in `html-report/`, with retry traces, screenshots, videos, and attachments in `test-results/`.
