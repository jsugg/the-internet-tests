# Java Selenium anti-patterns

The Java suite keeps a short rescue log of real defects fixed in this repository.
Use these examples when reviewing legacy browser automation code.

## Pre-rescue defects fixed in P3.1

| Anti-pattern | Example location | Safer pattern |
| --- | --- | --- |
| Waiting on the wrong element before reading feedback | `stacks/java-selenium-testng/src/test/java/theinternetwebsite/ui/pageobjects/LoginFormPage.java` — `getErrorMessage()` | Wait for the flash message itself, then read the returned visible element text. |
| Malformed regex character-class intersection | `stacks/java-selenium-testng/src/test/java/theinternetwebsite/ui/support/TextContent.java` — `clean()` | Put the intersection inside one character class: `[\\p{Cntrl}&&[^\\r\\n\\t]]`. |
| Calling `close()` before `quit()` during teardown | `stacks/java-selenium-testng/src/test/java/theinternetwebsite/ui/UITest.java` — `tearDown()` | Quit the session once; guard for failed setup so teardown preserves the original error. |
| Constructor navigation repeated inside a helper | `stacks/java-selenium-testng/src/test/java/theinternetwebsite/ui/testcases/LoginTest.java` — `successfulLogin()` (`UI-LOGIN-001`) | Reuse the page object already loaded by the test when submitting the form. |
| Suite parameters declared but ignored by tests | `stacks/java-selenium-testng/src/test/java/theinternetwebsite/ui/testcases/LoginTest.java` — `invalidUsername()` (`UI-LOGIN-002`) | Pass TestNG parameters through to the action; keep invalid defaults only as local fallbacks. |
| URL strings assembled with an accidental double slash | `stacks/java-selenium-testng/src/test/java/theinternetwebsite/ui/pageobjects/JavascriptErrorPage.java` — the `JavascriptErrorPage(UITest)` constructor | Pass the route to `BasePage`, which joins it through `UITest.urlFor()` with exactly one slash before navigation. |

## Download strategy split fixed in P3.4

| Anti-pattern | Example location | Safer pattern |
| --- | --- | --- |
| Verifying a browser download by fetching the link with raw HTTP | `stacks/java-selenium-testng/src/test/java/theinternetwebsite/ui/pageobjects/DownloadPage.java` | UI download scenarios click in the browser and wait for Selenium-managed downloads or the local download directory; future direct resource checks belong in an `@http` suite. |
