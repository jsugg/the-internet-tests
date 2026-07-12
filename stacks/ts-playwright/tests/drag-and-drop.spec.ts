import type { Page } from '@playwright/test';

import { expect, test } from './fixtures.js';

const boxA = '#column-a';
const boxB = '#column-b';

async function expectBoxLabels(page: Page, labels: [string, string]): Promise<void> {
  await expect(page.locator(boxA).locator('header')).toHaveText(labels[0]);
  await expect(page.locator(boxB).locator('header')).toHaveText(labels[1]);
}

async function dragBox(page: Page, source: string, target: string): Promise<void> {
  await page.locator(source).dragTo(page.locator(target));
}

test.describe('Drag and Drop', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/drag_and_drop');
    await expectBoxLabels(page, ['A', 'B']);
  });

  test('UI-DRAGDROP-001 @desktop drags box A over box B', async ({ page }) => {
    await dragBox(page, boxA, boxB);

    await expectBoxLabels(page, ['B', 'A']);
  });

  test('UI-DRAGDROP-002 @desktop drags box B over box A', async ({ page }) => {
    await dragBox(page, boxB, boxA);

    await expectBoxLabels(page, ['B', 'A']);
  });

  test('UI-DRAGDROP-003 @desktop drags boxes in both directions', async ({ page }) => {
    await dragBox(page, boxA, boxB);
    await expectBoxLabels(page, ['B', 'A']);

    await dragBox(page, boxB, boxA);
    await expectBoxLabels(page, ['A', 'B']);
  });
});
