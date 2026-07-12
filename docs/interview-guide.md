# Interview guide

Use the shared scenario catalog as the interview spine: identify the behavior, name the risk, then explain how each stack expresses the same test intent.

## Scenario talking points

| Scenario area | What interviewers look for | Strong answer |
| --- | --- | --- |
| Login and negative login | Authentication assertions and stable feedback checks | Assert the user-visible flash, cover valid and invalid paths, and avoid waiting on unrelated elements. |
| Add/remove, checkboxes, dropdown, inputs | Basic locator and state assertions | Prefer role/label/state locators; assert counts, values, checked state, and selected option text. |
| JavaScript alerts | Dialog orchestration | Register the dialog handler before the action, assert the message, and close the dialog deterministically. |
| Dynamic loading and controls | Async UI behavior | Wait for state transitions and visible outcomes, not fixed sleeps. |
| Upload/download | CI file paths and browser-vs-resource split | Upload uses a known fixture path; UI download uses browser download APIs; direct downloads belong in `@http`. |
| iFrame and windows | Context switching | Scope actions to the frame or new page and assert the new context, not just the click. |
| Status codes, redirects, auth, slow resources | Resource-layer testing | Use one Chromium/request-client slice per stack; do not multiply HTTP-only assertions across rendering engines. |
| Broken images | DOM plus HTTP hybrid | Extract image URLs from the page, then assert each resource response intentionally. |
| Drag and drop, hovers, context menu | desktop input semantics | Tag desktop-only behavior and explain why mobile emulation should not run it. |
| Shadow DOM | locator capabilities | Show that modern locator engines can cross open shadow roots while closed roots remain a design boundary. |
| Sortable tables | data modeling | Extract column data and assert sorted order instead of checking one lucky row. |
| Geolocation | browser permissions | Grant permission and set coordinates through the test framework before clicking the UI. |
| Infinite scroll and floating menu | viewport behavior | Use bounded scroll actions and viewport assertions; avoid unbounded loops. |
| Challenging DOM | bad testability | Avoid dynamic IDs; choose stable text, table structure, or user-observable affordances. |
| Flake lab | nondeterminism strategy | Demonstrate with tags, retries, and isolation, but keep unstable examples out of normal gates. |

## Stack-specific prompts

### Java Selenium/TestNG

- Where should browser setup live, and how does teardown preserve the original failure?
- Why are constructor navigation and public `WebElement` fields brittle?
- Which scenarios benefit from Page Objects, and which helper methods are over-abstraction?
- How does a local browser run differ from Grid or remote capabilities?

### TypeScript Playwright

- Which assertions are web-first and auto-waiting?
- When should a test use `page`, `context`, `request`, or a fixture?
- Why do projects make browser and device coverage explicit?
- How do traces, videos, and HTML reports change debugging workflow?

### Python Playwright

- How do pytest markers map to TypeScript tags?
- What should be shared as fixtures rather than module globals?
- How do mypy and Ruff keep test code reviewable?
- Where does Python add value for data-heavy SDET teams?

## Red flags to call out

- Sleeping instead of waiting for a user-visible condition.
- Asserting implementation details when a stable user contract exists.
- Running HTTP-only checks across multiple browser engines.
- Marking flaky tests as normal smoke tests.
- Hiding setup failures behind broad exception handling.
- Changing the catalog without adding or removing matching test IDs.
