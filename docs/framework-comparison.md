# Framework comparison guide

Use this guide to explain why the repository keeps three maintained tracks instead of treating one framework as universally best.

## Decision matrix

| Track | Best teaching use | Strengths | Tradeoffs |
| --- | --- | --- | --- |
| Java Selenium/TestNG | Legacy suite rescue, enterprise browser coverage, Page Object design | W3C WebDriver standard, broad Grid/vendor support, familiar Java interview surface | More lifecycle code, more explicit waits, and higher risk of brittle Page Objects |
| TypeScript Playwright | Modern flagship UI and resource testing | Auto-waiting locators, browser projects, request client, traces, HTML reports, mobile emulation | Tool-specific mental model; not a drop-in WebDriver replacement |
| Python Playwright | Python SDET/data-friendly comparison against the same catalog | Same browser engines as TypeScript Playwright, pytest markers, Python ergonomics | Smaller ecosystem surface than Java for legacy Selenium shops |
| Cypress | Comparison topic only | Popular JavaScript developer workflow and strong local runner | Not implemented here to avoid a fourth overlapping browser stack |
| WebdriverIO | Comparison topic only | Useful WebDriver-flavored JavaScript option | Lower marginal teaching value once Selenium plus Playwright are present |

## How to talk through choices

- Start from the scenario catalog, not the framework. The same scenario ID should explain what behavior matters before discussing implementation style.
- Use Selenium when the lesson is driver lifecycle, Grid, browser factory design, or legacy Page Object cleanup.
- Use Playwright when the lesson is locator design, auto-waiting, browser projects, traces, request-level checks, or mobile emulation.
- Keep HTTP/resource checks separate from rendering checks. Multiplying status-code or auth checks across every browser adds runtime without extra signal.
- Keep flake-lab examples tagged out of scheduled gates. Demonstrations are useful only if they do not make the build untrustworthy.

## Current JavaScript ecosystem context

As of 2026-07-12, the latest published State of JavaScript results are the 2025 edition. Its testing section is the citation to refresh when discussing JavaScript testing tool popularity or sentiment: [State of JavaScript 2025 — Testing](https://2025.stateofjs.com/en-US/libraries/testing/).

Use that citation as context, not as a mandate. This repository's implementation choices are driven by teaching coverage: Selenium for legacy/WebDriver literacy, TypeScript Playwright for the flagship modern stack, and Python Playwright for a second-language comparison.

## Related documentation

- [Documentation index](README.md) — every document, and which source wins on conflict.
- [ADR-0003 — Maintain three framework tracks](adr/0003-three-framework-tracks.md) — why Cypress and WebdriverIO are argued here but not implemented.
- [Architecture](architecture.md) — the stack boundaries this comparison depends on.
- [Learning paths](learning-paths.md) — where to go next, by audience.
