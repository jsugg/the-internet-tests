import { expect, test } from './fixtures.js';

test.describe('JQuery UI Menus', () => {
  test('UI-JQUERYUI-001 @desktop @not-ci models nested download options', async ({ page }) => {
    await page.goto('/jqueryui/menu');

    const menu = page.locator('#menu');

    await expect(menu.locator('a', { hasText: 'Enabled' })).toBeVisible();
    await expect(menu.locator('a[href$="menu.pdf"]')).toHaveText('PDF');
    await expect(menu.locator('a[href$="menu.csv"]')).toHaveText('CSV');
    await expect(menu.locator('a[href$="menu.xls"]')).toHaveText('Excel');
  });
});
