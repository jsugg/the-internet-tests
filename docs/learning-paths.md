# Learning paths

Each path uses the same repository artifacts in a different order. Start with the linked docs, then inspect the relevant stack tests and CI workflow.

## Beginner

1. Run the TypeScript Chromium smoke command from the root README.
2. Read the catalog rows for Login, Checkboxes, Dropdown, and Add/Remove Elements.
3. Compare the TypeScript and Python tests for those rows.
4. Add one local assertion and run the smoke slice again before reverting it.

## Interview candidate

1. Read [`docs/interview-guide.md`](interview-guide.md).
2. Study Dynamic Loading, Dynamic Controls, iFrame, Windows, Upload, Download, Broken Images, and Status Codes.
3. Practice explaining why UI downloads and direct HTTP downloads are separate scenarios.
4. Use the scenario matrix to identify which stack teaches each tradeoff best.

## Legacy Selenium maintainer

1. Read [`docs/anti-patterns.md`](anti-patterns.md).
2. Inspect Java driver setup, waits, Page Objects, and download handling.
3. Compare local browser factories with future Grid/nightly goals.
4. Review Surefire XML artifacts and TestNG suite XML files.

## Modern Playwright learner

1. Read [`docs/framework-comparison.md`](framework-comparison.md).
2. Study TypeScript fixtures, Playwright projects, and trace/report settings.
3. Run Chromium smoke, then full Chromium, then the `@desktop` P1 slice.
4. Compare the TypeScript and Python marker/tag conventions.

## Senior CI/architecture reviewer

1. Start with the scenario catalog and generated matrix.
2. Read [`docs/flakiness-guide.md`](flakiness-guide.md).
3. Inspect PR, per-stack, and Nightly workflow slices.
4. Check how artifacts are staged under `artifacts/<stack>/<run-id>/<slice>/`.
5. Review how `@http`, `@desktop`, `@mobile-emulation`, `@flaky-demo`, and `@not-ci` compose with browser projects.
