# Runbook: CI failure triage

A check went red. This is how to find out why without reading the whole workflow
file, and how to reproduce it locally so you are debugging your change rather
than the Actions UI.

Rule zero: **never fix a red gate by weakening it.** Widening a tag, adding a
retry, or relaxing a lint to get green converts a defect into a permanent lie.
If a failure is genuinely not your change, say so in the pull request and fix the
cause.

Every reproduction below runs from the repository root unless it says otherwise,
and assumes the demo app is up where the failing job had it:

```bash
docker compose -f docker/compose.yml up -d website
```

## Failure classes

| Failure class | Workflow · job | First diagnostic step | Artifacts | Reproduce locally |
| --- | --- | --- | --- | --- |
| Hygiene lint | `pr.yml` · `repo-hygiene` | Read the failing step's name: it says which linter | None; the log is the artifact | See [hygiene lint](#hygiene-lint) |
| Catalog drift | `pr.yml` · `scenario-catalog` | The diff in the log names the rows that disagree | None | `python3 tools/check-scenarios.py --write-matrix` |
| Per-stack static | `pr.yml` · `ts-smoke`, `py-smoke`, `java-smoke` | Type, lint, and format failures are deterministic and reproduce exactly | None | See [per-stack static](#per-stack-static-checks) |
| Smoke | `pr.yml` · `java-smoke`, `ts-smoke`, `py-smoke` | Open the uploaded trace or Surefire XML before rerunning | `artifacts/<stack>/<run-id>/smoke/` | See [smoke](#smoke) |
| Regression matrix leg | `java.yml`, `ts.yml`, `python.yml` | Note **which leg** failed; one browser failing is a different bug from all of them failing | `artifacts/<stack>/<run-id>/<slice>/` | See [regression leg](#regression-matrix-leg) |
| Nightly grid | `nightly.yml` · `java-nightly-grid` | Check whether the local-driver leg passed on the same browser | `artifacts/java/<run-id>/<slice>/surefire-reports/` | See [nightly grid](#nightly-grid) |

Path-filtered workflows only run when your change touches their stack, so a
regression workflow that did not run is not a failure. See
[`../ci-workflows.md`](../ci-workflows.md) for the trigger map.

## Hygiene lint

Five linters share one job, so read the step name first. Each is runnable alone:

```bash
docker run --rm -v "$PWD:/repo:ro" -w /repo rhysd/actionlint:1.7.12 -color
git ls-files -z '*.md' | xargs -0 npx --yes markdownlint-cli2@0.23.0
```

Commit-message failures name the offending commit and rule. The rules, and the
exact scope list, are in [`../../CONTRIBUTING.md`](../../CONTRIBUTING.md);
`commitlint.config.cjs` is the authority.

A link-check failure prints the file, the line, and the unreachable target. If
the target is genuinely unverifiable rather than wrong — an upstream host that
rate-limits — it belongs in `.lycheeignore` with a reason, not in a retry.

**"This pull request changes `.github/workflows/` but not `docs/ci-workflows.md`"**
is not a lint failure: the CI guide mirrors the workflows and your change made it
untrue. Update the guide. Use `[ci-guide-exempt]` in the pull request title only
when the workflow edit has no semantic effect.

## Catalog drift

The job regenerates the matrix and diffs it. It fails when the committed matrix,
or the README copy of it, disagrees with `scenarios/catalog.yml`:

```bash
python3 tools/check-scenarios.py --write-matrix
git diff --exit-code docs/scenario-matrix.md
```

The output tells you which of the two things happened:

- A **diff in `docs/scenario-matrix.md`** means you changed the catalog and did
  not regenerate. Commit the regenerated file.
- A **README embed diff** means the catalog moved and the README section 5 copy
  did not. Copy the table out of `docs/scenario-matrix.md`.
- **`catalog … ids missing tests`** or **`test ids missing catalog rows`** means
  the catalog and the tests genuinely disagree. Fix whichever is wrong — do not
  flip a `coverage:` flag to silence it.

## Per-stack static checks

These are deterministic: if CI failed, your machine will too.

```bash
cd stacks/ts-playwright
npm run typecheck
npm run lint
npm run format:check
```

```bash
cd stacks/python-playwright
ruff check .
ruff format --check .
mypy src tests
```

```bash
mvn -B -f stacks/java-selenium-testng/pom.xml clean test-compile
```

## Smoke

The smoke jobs run one browser against the demo app. Read the artifact before
rerunning: a Playwright trace usually shows the cause in one pass, and a rerun
that passes without explanation has taught you nothing.

```bash
cd stacks/ts-playwright
THE_INTERNET_BASE_URL=http://localhost:7080 npm run test:chromium:smoke
```

```bash
cd stacks/python-playwright
THE_INTERNET_BASE_URL=http://localhost:7080 pytest -m smoke --browser chromium
```

```bash
mvn -B -f stacks/java-selenium-testng/pom.xml -P CLI_Parameters test \
  -DsuiteXmlFile=src/test/resources/smoke.xml \
  -Dbrowser=chrome \
  -DheadlessBrowser=true \
  -DuseSeleniumGrid=false \
  -DwebAppAddress=http://localhost:7080 \
  -DtestRunnerAddress=http://localhost:7080
```

If a smoke test fails intermittently rather than consistently, stop and use
[`../flakiness-guide.md`](../flakiness-guide.md). Do not retag it to get green.

## Regression matrix leg

`fail-fast` is disabled, so every leg reports independently. Which legs failed is
the diagnosis:

- **One browser leg only** — a genuine engine-specific difference, or a locator
  that only holds in one engine.
- **Every leg** — the change is broken everywhere; the matrix is noise. Debug the
  default browser and ignore the rest.
- **Only the mobile-emulation legs** — a device-profile assumption, usually
  viewport or touch.

Reproduce one leg by naming its project. Substitute the failing leg's browser:

```bash
cd stacks/ts-playwright
THE_INTERNET_BASE_URL=http://localhost:7080 npx playwright test --project=firefox
```

```bash
cd stacks/python-playwright
THE_INTERNET_BASE_URL=http://localhost:7080 pytest --browser firefox
```

```bash
mvn -B -f stacks/java-selenium-testng/pom.xml -P CLI_Parameters clean test \
  -DsuiteXmlFile=src/test/resources/regression.xml \
  -Dbrowser=firefox \
  -DheadlessBrowser=true \
  -DuseSeleniumGrid=false \
  -DwebAppAddress=http://localhost:7080 \
  -DtestRunnerAddress=http://localhost:7080
```

## Nightly grid

`nightly.yml` runs Java twice per browser: once with a local driver, once against
Selenium Grid. Compare them before anything else, because the pair localizes the
fault for free:

- **Grid fails, local passes** — the fault is in the Grid path: session
  capabilities, the node image, or a client/node version mismatch. It is not your
  test logic.
- **Both fail** — an ordinary test failure that has nothing to do with Grid.

The Grid services are pinned to the same Selenium image digests the nightly job
uses, so a local Grid reproduces it. Start the app plus Grid from the repository
root:

```bash
docker compose -f docker/compose.yml -f docker/compose.grid.yml up -d
```

Then run against the Grid from the stack directory:

```bash
cd stacks/java-selenium-testng
mvn -P CLI_Parameters test \
  -DsuiteXmlFile=src/test/resources/regression.xml \
  -Dbrowser=remote-chrome \
  -DheadlessBrowser=true \
  -DuseSeleniumGrid=true \
  -DtestRunnerAddress=http://website:5000 \
  -DwebAppAddress=http://localhost:7080 \
  -DseleniumGridAddress=http://localhost:4444/wd/hub
```

Stop everything from the repository root when finished:

```bash
docker compose -f docker/compose.yml -f docker/compose.grid.yml down
```

A nightly failure gates nothing on its own, but it is the only signal for
browsers the pull-request gate never exercises. Do not let it stay red — a
permanently red nightly is the same as no nightly.

## Related documentation

- [Documentation index](../README.md) — every document, and which source wins on conflict.
- [CI workflows](../ci-workflows.md) — what runs, when, and where each job stages its reports.
- [Flakiness guide](../flakiness-guide.md) — for failures that are intermittent rather than wrong.
- [Architecture](../architecture.md) — the catalog, stacks, and artifact model behind these jobs.
