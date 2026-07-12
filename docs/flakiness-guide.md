# Flakiness guide

The flake lab is a deliberately isolated TypeScript Playwright slice for studying unstable UI patterns without weakening normal gates.

## Tags

- `@flaky-demo` marks examples that are safe to study locally but excluded from PR gates and nightly by default.
- `@not-ci` marks examples that are unsuitable for scheduled automation and should stay local/manual only.
- `@desktop` keeps mouse-only behavior out of mobile-emulation projects.

## Local commands

```bash
cd stacks/ts-playwright
THE_INTERNET_BASE_URL=http://localhost:7080 npm run test:flake-lab
```

Run `@not-ci` examples only when you are deliberately investigating the interaction:

```bash
cd stacks/ts-playwright
THE_INTERNET_BASE_URL=http://localhost:7080 npx playwright test --project=chromium --grep @not-ci
```

## What to study

| Scenario | Lesson |
| --- | --- |
| Disappearing Elements | Optional navigation items make exact menu assertions brittle. |
| Dynamic Content | Randomized copy or assets require retry-aware assertions and clear test intent. |
| Entry Ad / Exit Intent | Modal timing and mouse-leave behavior should not be part of a fast PR gate. |
| Typos | Nondeterministic text demonstrates why exact copy checks need stable contracts. |
| Shifting Content | Layout movement belongs in targeted visual or geometry checks, not broad smoke suites. |
| Large and Deep DOM | Traversal-heavy pages are useful for locator performance experiments. |
| JQuery UI Menus | Deep hover chains are desktop-only and sensitive to pointer sequencing. |
| A/B Testing | Experiment variants require either fixed test data or variant-tolerant assertions. |
