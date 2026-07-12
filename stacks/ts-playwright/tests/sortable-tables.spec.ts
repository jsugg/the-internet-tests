import type { Page } from '@playwright/test';

import { expect, test } from './fixtures.js';

async function columnText(page: Page, column: number): Promise<string[]> {
  return page
    .locator('#table1 tbody tr')
    .evaluateAll(
      (rows, columnIndex) =>
        rows.map((row) => row.children[columnIndex]?.textContent?.trim() ?? ''),
      column,
    );
}

test.describe('Sortable Tables', () => {
  test('UI-SORTTABLE-001 @desktop sorts table rows by last name and due amount', async ({
    page,
  }) => {
    await page.goto('/tables');

    await page.locator('#table1 thead').getByText('Last Name').click();
    await expect.poll(() => columnText(page, 0)).toEqual(['Bach', 'Conway', 'Doe', 'Smith']);

    await page.locator('#table1 thead').getByText('Due').click();
    await expect.poll(() => columnText(page, 3)).toEqual(['$50.00', '$50.00', '$51.00', '$100.00']);
  });
});
