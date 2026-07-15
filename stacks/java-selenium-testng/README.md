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

Run the Maven commands in this guide from this stack directory. There is no root `pom.xml`, so `mvn test` fails from the repository root. Docker Compose commands are the exception and run from the repository root, as each step below states.

Start The Internet demo app from the repository root:

```bash
docker compose -f docker/compose.yml up -d website
```

Run the suite from this stack directory:

```bash
cd stacks/java-selenium-testng
mvn test
```

Stop the app from the repository root:

```bash
docker compose -f docker/compose.yml down
```

## Optional local Selenium Grid

The retired `./run` script previously bundled app startup, Grid startup, and Maven execution. Docker Compose now owns local containers, while Maven stays the test entrypoint.

The Grid services are pinned to the same Selenium image digests used by the Nightly Grid workflow, so local reproduction and scheduled CI exercise the same browser-node family.

Start the app plus local Grid from the repository root:

```bash
docker compose -f docker/compose.yml -f docker/compose.grid.yml up -d
```

Run against the Grid from this stack directory:

```bash
cd stacks/java-selenium-testng
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

Stop all Compose services from the repository root:

```bash
docker compose -f docker/compose.yml -f docker/compose.grid.yml down
```

### Cloud Grid placeholders

The suite has one remote-driver seam: `-DseleniumGridAddress=<remote-webdriver-url>` with `-DuseSeleniumGrid=true`. Keep provider credentials outside the repository and inject them only in a private runner or a maintainer-approved workflow secret. No cloud secret is required for the checked-in Nightly Grid job; it uses the local Compose-equivalent Grid services above.

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
