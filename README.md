# The Internet Tests

[![PR](https://github.com/jsugg/the-internet-tests/actions/workflows/pr.yml/badge.svg?branch=master)](https://github.com/jsugg/the-internet-tests/actions/workflows/pr.yml)
![GitHub last commit](https://img.shields.io/github/last-commit/jsugg/the-internet-tests)
![GitHub issues](https://img.shields.io/github/issues/jsugg/the-internet-tests)
![GitHub pull requests](https://img.shields.io/github/issues-pr/jsugg/the-internet-tests)
![License](https://img.shields.io/github/license/jsugg/the-internet-tests)

## Overview

This repository is an educational automation playbook for [The Internet](https://the-internet.herokuapp.com/). The Java Selenium/TestNG suite is the current implemented stack. TypeScript Playwright and Python Playwright suites are on the roadmap so the same scenarios can be compared across frameworks.

## Stacks

- `stacks/java-selenium-testng/`: Java 25 + Maven + Selenium + TestNG implementation.
- TypeScript Playwright: roadmap.
- Python Playwright: roadmap.

## Prerequisites

- Java 25 LTS
- Maven 3.9 or newer
- Docker
- Chrome or Chromium for local browser execution

## Run the Java suite

Start the demo app:

```bash
docker run --rm -d --name the-internet -p 7080:5000 gprestes/the-internet:latest
```

Run the tests:

```bash
cd stacks/java-selenium-testng
mvn test
```

Stop the app when finished:

```bash
docker stop the-internet
```

See [`stacks/java-selenium-testng/README.md`](stacks/java-selenium-testng/README.md) for Java stack details and the legacy runner notes.

## Contributing

Open pull requests against `master` and include the verification commands you ran. Keep changes focused on one stack or scenario at a time.

## License

This project is licensed under the MIT License. See [LICENSE](LICENSE).
