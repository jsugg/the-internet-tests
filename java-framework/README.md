# Java Selenium TestNG stack

Java implementation of The Internet UI scenarios using Selenium WebDriver, TestNG, Maven, and Surefire reports.

## Prerequisites

- Java 25 LTS
- Maven 3.9 or newer
- Docker
- Chrome or Chromium

Selenium Manager provisions browser drivers automatically through Selenium. WebDriverManager is a common alternative worth comparing when learning the ecosystem, but this stack does not need it at runtime.

## Run locally

Start The Internet demo app:

```bash
docker run --rm -d --name the-internet -p 7080:5000 gprestes/the-internet:latest
```

Run the suite:

```bash
mvn test
```

Stop the app:

```bash
docker stop the-internet
```

## Legacy runner

The `./run` script is retained temporarily for existing users. It can still start the app, local Chrome runs, and the old Selenium Grid path, but Docker Compose becomes the supported entrypoint in a later phase.

```bash
./run -h
```

## Reports

Surefire output is written to:

```text
target/surefire-reports/
```

## Current scenario coverage

- Login success and failure
- Checkboxes
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
