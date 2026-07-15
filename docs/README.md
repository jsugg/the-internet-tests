# Documentation index

Every document in this repository, who it is for, whether it is authored or
generated, and what obliges someone to update it. If you are looking for where
to start reading rather than what exists, use
[`learning-paths.md`](learning-paths.md), which routes five audiences.

## Documents

Review cadence is a backstop, not a workflow: the update trigger is what
normally forces a change. Use Git history for versioning — no document carries a
version number or a "last updated" line, because both rot faster than the prose
they annotate.

| Document | Audience | Status | Update trigger | Review cadence |
| --- | --- | --- | --- | --- |
| [`../README.md`](../README.md) | Everyone; first contact with the repo | Authored | Stack, quick-start, support, or scope change | Every 6 months |
| [`../CONTRIBUTING.md`](../CONTRIBUTING.md) | Contributors | Authored | Setup, validation loop, commit or pull-request convention, or waiver change | Every 6 months |
| [`../.github/pull_request_template.md`](../.github/pull_request_template.md) | Contributors | Authored | Any change to the gates or documentation expectations it lists | Every 6 months |
| [`README.md`](README.md) (this file) | Anyone navigating the documentation | Authored | Any document added, removed, or repurposed | Every 6 months |
| [`architecture.md`](architecture.md) | Contributors; reviewers | Authored | New stack, catalog model change, or CI topology change | Annually |
| [`ci-workflows.md`](ci-workflows.md) | Contributors; reviewers | Authored | Any workflow semantic change, in the same pull request | Every 3 months |
| [`flakiness-guide.md`](flakiness-guide.md) | Contributors touching tags or retries | Authored | Retry, tagging, quarantine, or artifact policy change | Every 6 months |
| [`framework-comparison.md`](framework-comparison.md) | Learners; reviewers choosing a stack | Authored | A stack is added or dropped, or a dated external claim goes stale | Every 6 months |
| [`interview-guide.md`](interview-guide.md) | Interview candidates | Authored | Scenario coverage or emphasis change | Every 6 months |
| [`learning-paths.md`](learning-paths.md) | Learners, by audience | Authored | A document is added or removed, or audience routing changes | Every 6 months |
| [`anti-patterns.md`](anti-patterns.md) | Legacy Selenium maintainers | Authored | A defect is rescued, or a referenced symbol moves or is renamed | Every 6 months |
| [`scenario-matrix.md`](scenario-matrix.md) | Everyone | **Generated** | Never edit. Regenerate with `python3 tools/check-scenarios.py --write-matrix` whenever `scenarios/catalog.yml` changes | None; CI fails on drift |
| [`runbooks/ci-failure-triage.md`](runbooks/ci-failure-triage.md) | Anyone facing a red check | Authored | A job, gate, or reproduction command changes | Every 6 months |
| [`adr/README.md`](adr/README.md) | Contributors; reviewers | Authored | An ADR is added or superseded | Annually |
| [`adr/0000-template.md`](adr/0000-template.md) | ADR authors | Authored | The ADR structure changes | Annually |
| [`adr/0001-shared-scenario-catalog.md`](adr/0001-shared-scenario-catalog.md) | Contributors; reviewers | Authored | Superseding decision only | None |
| [`adr/0002-ui-http-scenario-split.md`](adr/0002-ui-http-scenario-split.md) | Contributors; reviewers | Authored | Superseding decision only | None |
| [`adr/0003-three-framework-tracks.md`](adr/0003-three-framework-tracks.md) | Contributors; reviewers | Authored | Superseding decision only | None |
| [`adr/0004-flaky-demo-not-ci-isolation.md`](adr/0004-flaky-demo-not-ci-isolation.md) | Contributors; reviewers | Authored | Superseding decision only | None |
| [`adr/0005-independent-workflow-topology.md`](adr/0005-independent-workflow-topology.md) | Contributors; reviewers | Authored | Superseding decision only | None |
| [`../stacks/java-selenium-testng/README.md`](../stacks/java-selenium-testng/README.md) | Java Selenium/TestNG track | Authored | Runtime, dependency, command, project, or report change | Every 6 months |
| [`../stacks/ts-playwright/README.md`](../stacks/ts-playwright/README.md) | TypeScript Playwright track | Authored | Runtime, dependency, command, project, or report change | Every 6 months |
| [`../stacks/python-playwright/README.md`](../stacks/python-playwright/README.md) | Python Playwright track | Authored | Runtime, dependency, command, project, or report change | Every 6 months |

## Which source wins

Documentation describes things that are themselves executable. When a document
and the thing it describes disagree, the executable artifact is right and the
document is a bug. Resolve conflicts in this order, highest authority first:

1. **[`../.github/workflows/`](../.github/workflows/)** — what CI actually runs. Canonical for triggers, jobs, steps, matrices, and pins.
2. **[`../scenarios/catalog.yml`](../scenarios/catalog.yml)** — canonical for which scenarios exist, their IDs, priorities, and which stacks claim coverage.
3. **[`scenario-matrix.md`](scenario-matrix.md)** — generated from the catalog. Authoritative over prose, but never over the catalog: if they disagree, the matrix was not regenerated.
4. **Stack READMEs** — canonical for how to run one stack.
5. **Guides** — this directory. Explain why, and defer to all of the above for what.

The root [`README.md`](../README.md) defers to every source above it for
specifics. It is a map, not an authority: its stack matrix and its embedded copy
of the scenario matrix are conveniences that must agree with the sources they
summarize.

Two documents restate a source of truth and can therefore drift from it:

- The root README section 5 scenario matrix is a copy of the generated matrix.
- [`ci-workflows.md`](ci-workflows.md) mirrors the workflows at step granularity.

Never repair either by editing the copy alone. Regenerate the matrix from the
catalog, and update the CI guide in the same pull request as the workflow change
that invalidated it.
