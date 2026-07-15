# Pull request

## What changed

<!-- Why this change exists. The diff already shows what; explain why. -->

## How it was verified

<!-- The commands you actually ran, and what they printed. -->

## Checklist

Tick each item, or replace it with one line saying why it does not apply. These
mirror the gates in `pr.yml` and the expectations in `CONTRIBUTING.md`.

- [ ] Scenario catalog (`scenarios/catalog.yml`) updated, or this change adds or removes no scenarios.
- [ ] Matrix regenerated with `python3 tools/check-scenarios.py --write-matrix`, and the README section 5 embed matches it.
- [ ] Every command I changed in the documentation was executed, not just edited.
- [ ] Documentation updated for this change type, per the lifecycle table.
- [ ] Workflow changes under `.github/workflows/` are mirrored in `docs/ci-workflows.md` in this pull request.
- [ ] No credentials or tokens, private endpoints, unredacted traces or screenshots, or real user data, per the secrets and captures rules in `CONTRIBUTING.md`.
- [ ] ADR added under `docs/adr/`, or this decision is not durable enough to need one.
