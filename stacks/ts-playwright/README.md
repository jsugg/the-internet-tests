# TypeScript Playwright stack

Playwright coverage for the shared scenario catalog. This stack currently starts with the `UI-LOGIN-001` smoke scenario and is expanded in later roadmap PRs.

## Prerequisites

- Node.js 22.13 or newer
- npm
- Docker, when running against the local The Internet app container
- Docker Compose

## Install

```bash
npm ci
npx playwright install --with-deps chromium
```

## Run locally

Start the demo app from the repository root:

```bash
docker compose -f docker/compose.yml up -d website
```

Run the smoke scenario:

```bash
THE_INTERNET_BASE_URL=http://localhost:7080 npm run test:chromium:smoke
```

Run static checks:

```bash
npm run typecheck
npm run lint
npm run format:check
```

Stop the demo app when finished:

```bash
docker compose -f docker/compose.yml down
```

## Browser projects

`playwright.config.ts` defines desktop Chromium, Firefox, WebKit, branded Chrome and Edge channels, Mobile Chrome, and Mobile Safari projects. PR CI runs only the Chromium smoke project to keep feedback fast.
