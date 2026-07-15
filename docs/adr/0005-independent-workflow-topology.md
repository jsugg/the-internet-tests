# ADR-0005 — Keep workflows independent and parallel

- **Status:** Accepted
- **Date:** 2026-07-10

## Context

Once the repository had a fast gate, three per-stack regressions, and a nightly
sweep, the obvious next move was to wire them together: make the regressions
depend on the smoke job, chain nightly off a successful run, add `concurrency:`
to cancel superseded runs.

Every one of those adds a coupling that has to be understood before anything can
be predicted. `needs:` turns a five-minute lint failure into a blocker for
everything behind it. `workflow_run:` makes the trigger invisible from the
workflow that runs. `concurrency:` cancels runs for reasons that look like
flakiness at the moment someone is trying to debug a failure. For a repository
whose purpose is to be *read* as a CI reference, that cost is paid on every
reading.

## Decision

Use no `needs:`, `workflow_run:`, `workflow_call:`, or `concurrency:` anywhere in
the five workflows. Every trigger fans out to its matching workflows in parallel;
inside each workflow every job runs in parallel. The only sequential execution is
the ordered list of steps within one job.

Accept the duplication this implies — each workflow checks out and sets up its
own toolchain — rather than factoring it into a called workflow. The duplication
is legible; the indirection would not be.

Where fan-out would waste work, solve it inside the job instead of across jobs:
each Playwright slice is planned before it runs, and a slice matching no tests is
skipped rather than failed.

## Consequences

- Any workflow can be read top to bottom and understood alone. "What will this
  change run?" is answered by the trigger and the path filter, nothing else.
- One failing leg never cancels another, so a run reports every independent
  failure at once instead of the first one. `fail-fast` is disabled on the
  matrices for the same reason.
- Redundant setup work runs in parallel across workflows, costing runner minutes
  that a `needs:` chain would have saved. That cost is accepted deliberately.
- Superseded runs are not cancelled, so pushing repeatedly to a pull request
  leaves runs in flight.
- Because nothing chains, no workflow can assume another has passed. The gate is
  the required-checks list on `master`, not workflow ordering.
- The full topology, triggers, jobs, and matrices are documented in
  [`../ci-workflows.md`](../ci-workflows.md), which is the mirror of the
  workflows themselves.
- Revisit if runner cost becomes the binding constraint, or if a job genuinely
  cannot run without another's artifact.
