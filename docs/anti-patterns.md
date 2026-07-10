# Java Selenium anti-patterns

The Java suite keeps a short rescue log of real defects fixed in this repository.
Use these examples when reviewing legacy browser automation code.

## Pre-rescue defects fixed in P3.1

| Anti-pattern | Example location | Safer pattern |
| --- | --- | --- |
| Waiting on the wrong element before reading feedback | `stacks/java-selenium-testng/src/test/java/theinternetwebsite/ui/pageobjects/LoginFormPage.java:58` | Wait for the flash message itself, then read the returned visible element text. |
| Malformed regex character-class intersection | `stacks/java-selenium-testng/src/test/java/theinternetwebsite/ui/UITest.java:186` | Put the intersection inside one character class: `[\\p{Cntrl}&&[^\\r\\n\\t]]`. |
| Calling `close()` before `quit()` during teardown | `stacks/java-selenium-testng/src/test/java/theinternetwebsite/ui/UITest.java:64` | Quit the session once; guard for failed setup so teardown preserves the original error. |
| Constructor navigation repeated inside a helper | `stacks/java-selenium-testng/src/test/java/theinternetwebsite/ui/testcases/LoginTest.java:29` | Reuse the page object already loaded by the test when submitting the form. |
| Suite parameters declared but ignored by tests | `stacks/java-selenium-testng/src/test/java/theinternetwebsite/ui/testcases/LoginTest.java:43` | Pass TestNG parameters through to the action; keep invalid defaults only as local fallbacks. |
| URL strings assembled with an accidental double slash | `stacks/java-selenium-testng/src/test/java/theinternetwebsite/ui/pageobjects/JavascriptErrorPage.java:23` | Join route paths with exactly one slash before navigation. |
