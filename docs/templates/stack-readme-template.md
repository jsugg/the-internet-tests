# `<Stack name>` stack

<!-- Skeleton for a new stack README. Fill every section or delete it with a
     reason; an empty section is worse than an absent one. The three existing
     stack READMEs are the worked examples. Placeholders are written as `<...>`;
     replace them, backticks and all. -->

One sentence: which framework, and which slice of the catalog this stack claims.

## Supported runtime versions

<!-- The exact versions CI pins, not a range you hope works. -->

| Component | Version |
| --- | --- |
| `<language runtime>` | `<version>` |
| `<test framework>` | `<version>` |
| `<build or package tool>` | `<version>` |

## Prerequisites

- `<prerequisite>`
- `<prerequisite>`

## Install

<!-- Delete this section only if the toolchain resolves dependencies implicitly,
     as Maven does — and say so rather than leaving it empty. -->

State the working directory before the first stack-local command.

```bash
cd stacks/<stack-dir>
<install command>
```

## Configuration

<!-- Every environment variable and flag this stack reads, with defaults.
     THE_INTERNET_BASE_URL is not optional documentation. -->

| Variable | Default | Purpose |
| --- | --- | --- |
| `THE_INTERNET_BASE_URL` | `<default>` | Base URL of the app under test |
| `<VAR>` | `<default>` | `<purpose>` |

## Fast validation

<!-- The single command that gives the quickest honest signal. -->

```bash
<fast command>
```

## Run locally

<!-- Compose commands run from the repository root; stack commands run from the
     stack directory. Say which, every time. -->

Start the demo app from the repository root:

```bash
docker compose -f docker/compose.yml up -d website
```

Run the suite from this stack directory:

```bash
cd stacks/<stack-dir>
<full run command>
```

Stop the app from the repository root:

```bash
docker compose -f docker/compose.yml down
```

## Static checks

<!-- The lint, format, and type commands CI runs, in CI's order. -->

```bash
<static check commands>
```

## CI mapping

<!-- Which jobs run this stack and when. Link the CI guide; do not restate its
     tables. -->

| Workflow | Job | Runs |
| --- | --- | --- |
| `pr.yml` | `<job>` | `<trigger>` |
| `<stack>.yml` | `<job>` | `<trigger>` |

## Reports and artifacts

| Output | Local path | CI path |
| --- | --- | --- |
| `<report>` | `<path>` | `artifacts/<stack>/<run-id>/<slice>/<path>` |

## Known limitations

<!-- What this stack does not cover, and why. Record the gap; do not hide it. -->

- `<limitation>`

## Adding or changing scenarios

<!-- How this stack carries the scenario ID, since the checker greps for it. -->

Where the ID goes in this stack, and the catalog-first workflow.

## Troubleshooting

<!-- Failures specific to this stack. General CI triage belongs in the runbook. -->

| Symptom | Cause | Fix |
| --- | --- | --- |
| `<symptom>` | `<cause>` | `<fix>` |

## Ownership

Who maintains this stack, and what triggers a review of this document.
