import { expect, test } from './fixtures.js';

test.describe('Multiple Windows', () => {
  test('UI-WINDOWS-001 opens expected content in a new tab', async ({ page }) => {
    await page.goto('/windows');

    const popupPromise = page.waitForEvent('popup');
    await page.getByRole('link', { name: 'Click Here' }).click();
    const popup = await popupPromise;

    await expect(popup.getByRole('heading', { name: 'New Window' })).toBeVisible();
    await popup.close();
  });
});
