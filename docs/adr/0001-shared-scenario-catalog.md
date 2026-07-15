# ADR-0001 — Shared scenario catalog as the canonical model

- **Status:** Accepted
- **Date:** 2026-07-08

## Context

The repository exists to compare automation stacks against the same application.
That comparison is only meaningful if the stacks are answering the same
questions. Without a shared definition of "the scenarios", each stack would drift
into testing whatever its examples happened to cover, and any claim that one
stack covers more than another would be unfalsifiable — the coverage tables would
be prose, maintained by whoever last remembered.

The obvious alternatives each fail differently. Deriving coverage from test names
alone makes the tests self-certifying: a stack covers whatever it says it covers,
and a missing test is invisible because nothing declares it should exist. Keeping
a hand-written coverage table in the README makes the table a second source of
truth that rots on the first merge.

## Decision

Make [`scenarios/catalog.yml`](../../scenarios/catalog.yml) the canonical list of
scenarios. Every scenario has a stable ID matching `(?:UI|HTTP)-[A-Z0-9-]+`, a
title, a priority, and a `coverage:` map with one boolean per stack. Tests
reference the ID; the catalog leads and the tests follow.

Enforce it with [`tools/check-scenarios.py`](../../tools/check-scenarios.py),
which reconciles catalog and tests in **both** directions and fails on a catalog
row marked covered with no test, a test ID with no catalog row, or a test whose
row is not marked covered. The coverage matrix is generated from the catalog, not
written by hand.

## Consequences

- Coverage becomes a fact that CI checks, not a claim. Uneven coverage across
  stacks is recorded honestly rather than hidden.
- Adding a scenario costs a catalog edit before the test. This friction is the
  point: it forces the scenario to be named and prioritized before it is
  implemented.
- The catalog is parsed with a line-oriented reader rather than a YAML library,
  so the file's formatting is load-bearing. Keep new rows shaped like existing
  ones.
- Reconciliation matches IDs by regular expression, so a green run proves the
  catalog and the test sources agree about names — not that a test asserts
  anything. See [ADR-0002](0002-ui-http-scenario-split.md) for the ID grammar and
  [`../architecture.md`](../architecture.md) for the caveat in full.
- Revisit if the catalog outgrows a flat list — for example if scenarios need
  hierarchy, per-stack parameters, or ownership metadata.
