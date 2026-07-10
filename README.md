# The Internet Tests

[![PR](https://github.com/jsugg/the-internet-tests/actions/workflows/pr.yml/badge.svg?branch=master)](https://github.com/jsugg/the-internet-tests/actions/workflows/pr.yml)
![GitHub last commit](https://img.shields.io/github/last-commit/jsugg/the-internet-tests)
![GitHub issues](https://img.shields.io/github/issues/jsugg/the-internet-tests)
![GitHub pull requests](https://img.shields.io/github/issues-pr/jsugg/the-internet-tests)
![License](https://img.shields.io/github/license/jsugg/the-internet-tests)

## Overview

This repository is an educational automation playbook for [The Internet](https://the-internet.herokuapp.com/). The Java Selenium/TestNG suite is the full current stack, and the TypeScript Playwright stack now covers its first shared smoke scenario. Python Playwright remains on the roadmap so the same scenarios can be compared across frameworks.

## Stacks

- `stacks/java-selenium-testng/`: Java 25 + Maven + Selenium + TestNG implementation.
- `stacks/ts-playwright/`: TypeScript + Playwright implementation, currently seeded with `UI-LOGIN-001`.
- Python Playwright: roadmap.

## Prerequisites

- Java 25 LTS
- Maven 3.9 or newer
- Node.js 22.13 or newer
- npm
- Docker
- Docker Compose
- Chrome or Chromium for local browser execution

## Start the demo app

Use Docker Compose from the repository root:

```bash
docker compose -f docker/compose.yml up -d website
```

Run the Java tests:

```bash
cd stacks/java-selenium-testng
mvn test
```

Run the TypeScript smoke tests:

```bash
cd stacks/ts-playwright
THE_INTERNET_BASE_URL=http://localhost:7080 npm run test:chromium:smoke
```

Stop the app from the repository root:

```bash
docker compose -f docker/compose.yml down
```

See [`stacks/java-selenium-testng/README.md`](stacks/java-selenium-testng/README.md) for Java stack details and local Grid usage. See [`stacks/ts-playwright/README.md`](stacks/ts-playwright/README.md) for TypeScript stack commands.

## Contributing

Open pull requests against `master` and include the verification commands you ran. Keep changes focused on one stack or scenario at a time.

## License

This project is licensed under the MIT License. See [LICENSE](LICENSE).
