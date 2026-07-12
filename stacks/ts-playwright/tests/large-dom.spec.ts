import { expect, test } from './fixtures.js';

test.describe('Large and Deep DOM', () => {
  test('UI-LARGEDOM-001 @not-ci reaches deep nodes and large-table cells', async ({ page }) => {
    await page.goto('/large');

    await expect(page.locator('#sibling-50\\.3')).toHaveText('50.3');
    await expect(page.locator('#large-table tbody tr')).toHaveCount(50);
    await expect(page.locator('#large-table tbody tr').nth(49).locator('td').nth(49)).toHaveText(
      '50.50',
    );
  });
});
