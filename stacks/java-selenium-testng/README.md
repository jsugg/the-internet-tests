# Java Selenium TestNG stack

Java implementation of The Internet UI scenarios using Selenium WebDriver, TestNG, Maven, and Surefire reports.

## Prerequisites

- Java 25 LTS
- Maven 3.9 or newer
- Docker
- Docker Compose
- Chrome or Chromium

Selenium Manager provisions browser drivers automatically through Selenium. WebDriverManager is a common alternative worth comparing when learning the ecosystem, but this stack does not need it at runtime.

## Run locally

Start The Internet demo app from the repository root:

```bash
docker compose -f docker/compose.yml up -d website
```

Run the suite:

```bash
mvn test
```

Stop the app:

```bash
docker compose -f docker/compose.yml down
```

## Optional local Selenium Grid

The retired `./run` script previously bundled app startup, Grid startup, and Maven execution. Docker Compose now owns local containers, while Maven stays the test entrypoint.

Start the app plus local Grid from the repository root:

```bash
docker compose -f docker/compose.yml -f docker/compose.grid.yml up -d
```

Run against the Grid:

```bash
mvn -P CLI_Parameters test \
  -DsuiteXmlFile=src/test/resources/regression.xml \
  -Dbrowser=remote-chrome \
  -DbrowserVersion= \
  -DheadlessBrowser=true \
  -DuseSeleniumGrid=true \
  -DtestsThreadCount=1 \
  -DtestRunnerAddress=http://website:5000 \
  -DwebAppAddress=http://localhost:7080 \
  -DseleniumGridAddress=http://localhost:4444/wd/hub
```

Stop all Compose services:

```bash
docker compose -f docker/compose.yml -f docker/compose.grid.yml down
```

## Reports

Surefire output is written to:

```text
target/surefire-reports/
```

CI stages the XML files under `artifacts/java/<run-id>/<slice>/surefire-reports/` before uploading each Java workflow artifact. Allure is intentionally deferred until it can stay low-friction beside Surefire.

## Current scenario coverage

- Login success and failure
- Checkboxes
- Challenging DOM
- Context menu
- Drag and drop
- Dropdown
- Dynamic content
- Dynamic controls
- Dynamic loading
- File download
- File upload
- Floating menu
- iFrame
- Hovers
- JavaScript alerts
- JavaScript error
- Multiple windows
- Notification message
