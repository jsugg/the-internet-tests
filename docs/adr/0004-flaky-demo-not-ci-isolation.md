# ADR-0004 — Isolate flaky-demo and not-ci tests from the gates

- **Status:** Accepted
- **Date:** 2026-07-10

## Context

This repository deliberately contains unstable tests. The flake lab exists to
study the patterns that make suites unreliable — races, animations,
nondeterministic copy, shifting layout — and those examples are only instructive
if they actually misbehave. A demo app page that randomizes its own content
cannot be made reliable by better waiting; that is the lesson.

That creates a direct conflict with the gates. A suite that fails at random
trains people to ignore red, which is the exact habit the repository argues
against. The tempting resolutions are both wrong: deleting the unstable examples
removes the teaching material, and retrying until green teaches that flakiness is
a retry budget rather than a defect.

## Decision

Keep the unstable material and tag it out of the gates rather than removing it or
papering over it:

- `@flaky-demo` / `flaky_demo` — deliberately unstable teaching examples.
  Excluded from the pull-request gate and from nightly by default, and included
  in nightly only when the `include-flaky-demo` dispatch input is true.
- `@not-ci` / `not_ci` — examples unsuitable for unattended automation. Excluded
  everywhere in CI; run locally, on purpose.

Everything unmarked is expected to be deterministic, and a failure there is a
real defect. Never widen a tag to silence a genuine failure: the triage order is
recorded in [`../flakiness-guide.md`](../flakiness-guide.md), and retagging is
the last step, taken only once the nondeterminism is understood and attributable
to the application rather than the test.

## Consequences

- The gates stay trustworthy: red means broken. That is what makes requiring the
  checks on `master` reasonable at all.
- The flake lab remains runnable and studyable on demand, and nightly can opt in.
- The tags are load-bearing and abusable. `@flaky-demo` on a test that is merely
  poorly written hides a real bug and converts a defect into a teaching exhibit;
  the guide's triage checklist exists to make that misuse harder.
- Python declares its markers with `--strict-markers`, so a typo fails instead of
  silently matching nothing — a mistyped tag would otherwise quietly re-admit an
  unstable test to the gate.
- Revisit if unmarked tests start failing intermittently, which would mean the
  boundary is in the wrong place.
