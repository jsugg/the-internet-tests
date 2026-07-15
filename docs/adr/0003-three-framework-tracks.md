# ADR-0003 — Maintain three framework tracks

- **Status:** Accepted
- **Date:** 2026-07-11

## Context

A comparison repository has to choose how many things to compare. Too few and
there is no contrast; too many and every scenario must be written several more
times, the coverage gaps multiply, and the repository becomes a museum of
half-finished stacks — which teaches the opposite of the intended lesson.

Three audiences were worth serving: the maintainer inheriting a legacy Selenium
suite, the engineer learning the modern default, and the Python team that cannot
adopt a Node-only toolchain. Each additional stack costs a full implementation of
every scenario it claims, forever.

## Decision

Maintain exactly three tracks, each with its own toolchain, dependency manifest,
and README, sharing no code:

| Track | Serves |
| --- | --- |
| Java Selenium/TestNG | The legacy-maintainer audience |
| TypeScript Playwright | The modern flagship |
| Python Playwright | Teams whose language is Python |

Treat Cypress and WebdriverIO as **comparison topics only**, documented but not
implemented, for the reasons recorded in
[`../framework-comparison.md`](../framework-comparison.md): Cypress would add a
fourth overlapping browser stack, and WebdriverIO has low marginal teaching value
once Selenium and Playwright are both present. Argue their tradeoffs in prose
rather than paying their maintenance cost in tests.

Deliberately do not extract a shared abstraction layer across the three tracks.
The differences between them are the subject matter; hiding those differences
behind a common wrapper would destroy the thing being taught.

## Consequences

- Every scenario can cost up to three implementations. Coverage is therefore
  intentionally uneven — TypeScript carries the most, Java the legacy subset,
  Python the P0 suite plus selected P1 work — and the generated matrix records
  that rather than hiding it.
- Duplication across stacks is expected and correct here. Do not refactor it away.
- A reader can compare real, idiomatic code in each framework instead of reading
  a claim about how they differ.
- Cypress and WebdriverIO opinions in the comparison are not backed by tests in
  this repository, and should be read as reasoning, not evidence.
- Revisit if an audience appears that none of the three serves, or if a track
  stops being maintained — an unmaintained track is worse than an absent one.
