# ADR-0002 — Separate UI scenarios from HTTP/resource scenarios

- **Status:** Accepted
- **Date:** 2026-07-10

## Context

Some of the demo application's behavior is not about rendering: status codes,
redirects, basic and digest authentication, slow resources, and broken images are
properties of HTTP responses. A browser can observe them, but driving a browser
to assert a status code is theatre — it is slower, flakier, and teaches the wrong
instinct.

The cost showed up in the browser matrix. Running a status-code check in
Chromium, Firefox, and WebKit repeats an identical assertion three times and
proves nothing about the second and third: the response does not change because
the rendering engine did.

## Decision

Split the catalog by prefix. `UI-` scenarios drive a browser and assert rendered
behavior. `HTTP-` scenarios assert resource-layer behavior and make no rendering
claims. The prefix is part of the scenario ID, so the split is visible in the ID
grammar, the matrix, and every test name.

Pin `HTTP-` scenarios to a single browser project in CI with the `@http` / `http`
tag, and invert them out of the other projects so they run once per run rather
than once per browser.

## Consequences

- The browser matrix stays honest: a matrix leg exists to test a rendering
  engine, so only scenarios that depend on one fan out across it.
- Faster runs and fewer duplicate failures. One broken redirect fails once, not
  once per browser.
- The Java track has no `HTTP-` coverage, which the matrix shows as a real gap
  rather than an oversight. Direct resource checks in Java belong in a dedicated
  `@http` suite if that track ever needs them.
- A new scenario now requires a judgment before it is written: is this about what
  the browser renders, or about what the server returned? Getting it wrong is
  visible — an `HTTP-` scenario that needs the DOM is misfiled.
- Revisit if a scenario legitimately spans both layers and the prefix stops
  describing it.
