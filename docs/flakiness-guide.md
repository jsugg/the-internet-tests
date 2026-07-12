# Flakiness guide

The flake lab is a deliberately isolated TypeScript Playwright slice for studying unstable UI patterns without weakening normal gates.

## Operating model

- `@flaky-demo` marks examples that are safe to study locally but excluded from PR gates and nightly by default.
- `@not-ci` marks examples that are unsuitable for scheduled automation and should stay local/manual only.
- `@desktop` keeps mouse-only behavior out of mobile-emulation projects.
- Retries are diagnostic, not a correctness strategy. A retry can expose nondeterminism, but the root cause still needs a tag, isolation rule, or product/test contract change.

## Local commands

```bash
cd stacks/ts-playwright
THE_INTERNET_BASE_URL=http://localhost:7080 npm run test:flake-lab
```

Run `@not-ci` examples only when you are deliberately investigating the interaction:

```bash
cd stacks/ts-playwright
THE_INTERNET_BASE_URL=http://localhost:7080 npx playwright test --project=chromium --grep @not-ci
```

## Failure taxonomy

| Cause | Example scenario | Safer pattern |
| --- | --- | --- |
| Randomized content | Dynamic Content, Typos, A/B Testing | Assert allowed variants or stabilize test data. |
| Timing-sensitive UI | Entry Ad, Exit Intent | Scope to local/manual runs unless the trigger is deterministic. |
| Optional DOM elements | Disappearing Elements | Assert the stable core plus an allowed optional set. |
| Layout movement | Shifting Content | Use bounded geometry checks or move to visual tooling. |
| Heavy traversal | Large and Deep DOM | Prefer targeted selectors and keep performance experiments out of smoke. |
| Pointer sequencing | JQuery UI Menus, Hovers, Context Menu | Tag as desktop-only and avoid mobile projects. |
| External/resource timing | Slow Resources | Use bounded request timeouts and assert the timeout contract. |

## Triage checklist

1. Reproduce with the smallest tag slice.
2. Capture trace/video only around the failing slice.
3. Identify whether the product contract is stable, variable, or intentionally random.
4. If stable, fix the wait or locator.
5. If variable, encode the allowed variants or move the scenario to `@flaky-demo`.
6. If unsuitable for CI, add `@not-ci` and document the manual learning value.
7. Never weaken catalog reconciliation, linting, typechecking, or CI gates to hide the failure.
