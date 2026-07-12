import type { Page } from '@playwright/test';

import { expect, test } from './fixtures.js';

type DynamicContentSnapshot = readonly string[];

async function contentSnapshot(page: Page, staticImages: boolean): Promise<DynamicContentSnapshot> {
  await page.goto(`/dynamic_content${staticImages ? '?with_content=static' : ''}`);

  return page
    .locator('.large-10.columns')
    .evaluateAll((nodes) =>
      nodes.map((node) => node.textContent?.replace(/\s+/g, ' ').trim() ?? ''),
    );
}

async function expectContentToChange(page: Page, staticImages: boolean): Promise<void> {
  const first = await contentSnapshot(page, staticImages);
  let changed = false;

  for (let attempt = 0; attempt < 5; attempt += 1) {
    const next = await contentSnapshot(page, staticImages);
    if (JSON.stringify(next) !== JSON.stringify(first)) {
      changed = true;
      break;
    }
  }

  expect(changed).toBe(true);
}

test.describe('Dynamic Content', () => {
  test('UI-DYNCONTENT-001 @flaky-demo observes fully dynamic content changes', async ({ page }) => {
    await expectContentToChange(page, false);
  });

  test('UI-DYNCONTENT-002 @flaky-demo observes text changes with static images', async ({
    page,
  }) => {
    await expectContentToChange(page, true);
  });
});
