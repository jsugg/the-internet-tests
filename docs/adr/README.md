# Architecture decision records

Decisions that shaped this repository, with the reasoning that produced them.
The code shows what the repository does; these records exist for the part the
code cannot show — what was rejected, and why.

## What warrants an ADR

Write one when a decision is hard to reverse and affects any of:

- More than one stack.
- The scenario catalog or its model.
- CI topology: what runs, when, and what gates a merge.
- Tagging semantics.
- Artifact contracts.
- Contributor workflow.

Everything else is a pull request. A decision that is cheap to undo does not
need a record; a decision someone will otherwise re-litigate in six months does.
If you are unsure, ask whether a reader would be able to reconstruct the
reasoning from the diff alone. If not, write the ADR.

## How to add one

1. Copy [`0000-template.md`](0000-template.md) to `NNNN-short-title.md`, taking
   the next free number.
2. Fill in Status, Date, Context, Decision, and Consequences.
3. Add it to the table below, and to the
   [documentation index](../README.md).

ADRs are immutable once accepted. A decision that no longer holds is not edited
and not deleted: write a new ADR and set the old one's status to **Superseded
by** the new number. The record of having been wrong is the useful part.

## Records

| ADR | Title | Status | Date |
| --- | --- | --- | --- |
| [0001](0001-shared-scenario-catalog.md) | Shared scenario catalog as the canonical model | Accepted | 2026-07-08 |
| [0002](0002-ui-http-scenario-split.md) | Separate UI scenarios from HTTP/resource scenarios | Accepted | 2026-07-10 |
| [0003](0003-three-framework-tracks.md) | Maintain three framework tracks | Accepted | 2026-07-11 |
| [0004](0004-flaky-demo-not-ci-isolation.md) | Isolate flaky-demo and not-ci tests from the gates | Accepted | 2026-07-10 |
| [0005](0005-independent-workflow-topology.md) | Keep workflows independent and parallel | Accepted | 2026-07-10 |
