# Runbook: `<situation>`

<!-- Skeleton for an operational runbook. A runbook is read by someone who is
     already blocked, so lead with the fastest route to a diagnosis, not with
     background. docs/runbooks/ci-failure-triage.md is the worked example.
     Placeholders are written as `<...>`; replace them, backticks and all. -->

One sentence: what has gone wrong, and what this runbook gets you to.

<!-- State the rule that must not be broken while fixing this, if there is one.
     Example: never fix a red gate by weakening it. -->

Preconditions: what must be running or set up before any step below works.

```bash
<setup command, if any>
```

## Failure classes

<!-- One row per distinguishable failure. The point of the table is that a
     reader can identify their row in seconds and skip the rest. -->

| Failure class | Where it surfaces | First diagnostic step | Artifacts | Reproduce locally |
| --- | --- | --- | --- | --- |
| `<class>` | `<workflow · job, or system>` | `<the one check that discriminates>` | `<path>` | `<command, or a link to its section>` |
| `<class>` | `<where>` | `<first step>` | `<path>` | `<command>` |

## Failure class: `<first class>`

<!-- One section per class that needs more than a table cell. Say what the
     failure means before saying what to type. -->

What this failure actually indicates.

```bash
<reproduction command>
```

How to tell a real defect from an environmental failure, and what to do in each
case.

## Failure class: `<second class>`

What this failure actually indicates.

```bash
<reproduction command>
```

## Related documentation

<!-- Two to four links, including the documentation index. -->

- [Documentation index](../README.md) — every document, and which source wins on conflict.
- `<link>` — why a reader here would want it.
