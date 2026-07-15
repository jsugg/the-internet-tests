# Contributing

This is a teaching repository: the tests, the catalog, and the CI wiring are the
product. A change is finished when someone reading the documentation can
reproduce it, not when it passes locally. Read [`README.md`](README.md) for what
the repo is and is not before proposing new scope.

## Repository setup

Start the demo app under test from the repository root. Every stack runs against
it on `http://localhost:7080`:

```bash
docker compose -f docker/compose.yml up -d website
```

Set up only the stacks you are changing. Each stack is self-contained and runs
from its own directory:

```bash
cd stacks/ts-playwright
npm ci
npx playwright install --with-deps chromium
```

```bash
cd stacks/python-playwright
python3 -m venv .venv
source .venv/bin/activate
python -m pip install --upgrade pip
python -m pip install -e ".[dev]"
python -m playwright install --with-deps chromium
```

The Java stack needs no install step; Maven resolves dependencies on first run,
and Selenium Manager provisions the browser drivers.

Stop the app from the repository root when you are done:

```bash
docker compose -f docker/compose.yml down
```

Each stack README is the authority on running that stack:
[Java](stacks/java-selenium-testng/README.md),
[TypeScript](stacks/ts-playwright/README.md),
[Python](stacks/python-playwright/README.md).

## Fast validation loop

Run these two from the repository root before every push. They are the same
checks the `repo-hygiene` and `scenario-catalog` PR jobs run, and they need no
browsers and no app:

```bash
python3 tools/check-scenarios.py
git ls-files -z '*.md' | xargs -0 npx --yes markdownlint-cli2@0.23.0
```

Then run the static checks for the stacks you touched:

```bash
mvn -B -f stacks/java-selenium-testng/pom.xml clean test-compile
```

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

If you changed a workflow, lint it with the same pinned image CI uses, from the
repository root:

```bash
docker run --rm -v "$PWD:/repo:ro" -w /repo rhysd/actionlint:1.7.12 -color
```

## Adding or changing a scenario

The catalog is the canonical list of scenarios; the tests reference it, and
`tools/check-scenarios.py` reconciles the two in both directions. A scenario that
is not in the catalog is not covered, no matter how many tests exist. Work in
this order:

1. Add or edit the row in [`scenarios/catalog.yml`](scenarios/catalog.yml). Give
   it a unique ID, a priority, a title, and a `coverage:` entry per stack.
2. Add the tests, referencing the scenario ID. Java requires the ID in the
   TestNG `testName="…"` attribute; TypeScript and Python carry it in the test
   title or marker.
3. Set `coverage:` to `true` only for the stacks that actually have a test. The
   checker fails in both directions: a catalog row marked covered with no test,
   and a test whose ID is missing or not marked covered.
4. Regenerate the matrix and sync the README embed from the repository root:

   ```bash
   python3 tools/check-scenarios.py --write-matrix
   ```

Never hand-edit [`docs/scenario-matrix.md`](docs/scenario-matrix.md); it is
generated, and the `scenario-catalog` job fails on any drift.

## Commit conventions

Commits are linted by commitlint in CI against
[`commitlint.config.cjs`](commitlint.config.cjs), which is the authority. The
rules:

- Conventional commits: `type(scope): subject`.
- Type is one of `feat`, `fix`, `docs`, `ci`, `build`, `refactor`, `test`,
  `chore`, `perf`, lower-case.
- Scope is optional, lower-case, and if present must be one of `java`, `ts`,
  `py`, `catalog`, `docker`, `actions`, `deps`, `readme`. There is deliberately
  no `docs` or `ci` scope: use a scopeless `docs: …` for governance docs and
  `ci(actions): …` for workflow changes.
- The header is at most 72 characters, and the subject takes no trailing period.
- Body and footer lines are at most 100 characters.

Explain why in the body, not what; the diff already shows what. Do not add
attribution trailers, and do not name tooling vendors or assistants in branch
names, commit messages, or pull requests.

## Pull requests

Fill in the [pull request template](.github/pull_request_template.md); it is the
change checklist, not a formality. Squash-merge is the norm, so the PR title
becomes the squashed subject and must satisfy the same commitlint rules.

Every pull request runs the always-on fast gate (`pr.yml`): repository hygiene,
scenario-catalog drift, and a compile-plus-smoke slice per stack. Regression
workflows are path-filtered and only run when your change touches their stack.
See [`docs/ci-workflows.md`](docs/ci-workflows.md) for the full trigger fan-out.

## Secrets and captures

This is a teaching repository with no deployed service, no users, and no data of
its own, so the realistic risk here is not an exploit. It is committing something
that should never have been public, into a repository that is public on purpose.

- **Never commit a credential or token.** Not an API key, a provider token, a
  session cookie, or a private key — including expired and throwaway ones. Secret
  scanning and push protection are enabled on this repository, so a detected
  secret blocks the push. Treat that as the backstop, not the plan.
- **Point the stacks at the demo app, not at a real system.** The moment a stack
  runs against something real, its traces and screenshots inherit that system's
  secrets. Use the pinned container on `http://localhost:7080`.
- **Redact captures before attaching them.** A Playwright trace records request
  headers in full. Open a trace, screenshot, or log before you put it in a pull
  request.
- **Keep `artifacts/` out of Git.** It is git-ignored staging for CI upload, and
  CI artifacts on a public repository are downloadable by anyone.
- **Keep remote-grid credentials outside the repository**, injected only through
  a private runner or a maintainer-approved workflow secret, as the
  [Java stack README](stacks/java-selenium-testng/README.md) says. No
  checked-in workflow needs one.

The demo app's `tomsmith` / `SuperSecretPassword!` login is a published fixture
of a public teaching app, not a secret. It belongs in the tests.

## Documentation compliance

A change is documentation-compliant when all five hold:

1. A new contributor can reproduce the affected behavior from the documented
   commands, without reading the diff.
2. Canonical documentation agrees with the executable configuration it
   describes.
3. Cross-stack design changes are recorded, so the reasoning outlives the PR.
4. Documentation responsibility for the change is identifiable.
5. No required documentation validation fails.

## Documentation expectations

Update the documentation in the same pull request as the change. What to update
depends on what you changed. [`docs/README.md`](docs/README.md) carries the full
lifecycle table — every document's audience, update trigger, and review cadence —
plus the authority order to apply when two sources disagree, and the
documentation conventions to follow when writing: terminology, headings, dates,
shell assumptions, source references, diagrams, and generated files.

| Change | Update |
| --- | --- |
| A workflow under `.github/workflows/` | [`docs/ci-workflows.md`](docs/ci-workflows.md), in the same pull request. CI enforces this. |
| A scenario or the catalog | Regenerate `docs/scenario-matrix.md` and sync the README §5 embed. |
| A stack's commands, runtime, or reports | That stack's README, and the root README stack matrix if the headline command changed. |
| Retry, tagging, quarantine, or artifact policy | [`docs/flakiness-guide.md`](docs/flakiness-guide.md). |
| A durable cross-stack, CI, or catalog decision | A new ADR under `docs/adr/`. |
| Scope, quick start, or supported stacks | [`README.md`](README.md). |

## Recorded waivers

Requirements this repository deliberately does not meet, with the condition that
would reopen each one. Both exist because this is a single-maintainer repository;
neither is an oversight.

### W-001 — `CODEOWNERS` omitted

| Field | Value |
| --- | --- |
| Requirement | Code-owner review via a `CODEOWNERS` file |
| Scope | Whole repository |
| Reason | The only owner entry possible today is `* @jsugg`. Code-owner review would then demand an approval that the sole maintainer cannot give on their own pull requests, deadlocking every change. |
| Owner | @jsugg |
| Approver | @jsugg |
| Revisit trigger | First sustained external contributor |
| Tracking issue | None; repository issues are disabled, so the trigger above is the record |

### W-002 — Reviewer-assignment machinery omitted

| Field | Value |
| --- | --- |
| Requirement | Owner maps, required reviewers, and automatic reviewer assignment |
| Scope | Whole repository |
| Reason | There is one maintainer and no reviewer pool to route to. Required reviewers would block every pull request for the same reason as W-001, and an owner map would encode a single name already visible in the repository metadata. |
| Owner | @jsugg |
| Approver | @jsugg |
| Revisit trigger | First sustained external contributor |
| Tracking issue | None; repository issues are disabled, so the trigger above is the record |
