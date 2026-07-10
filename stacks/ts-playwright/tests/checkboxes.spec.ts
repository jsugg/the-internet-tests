import { expect, test } from './fixtures.js';

test.describe('Checkboxes', () => {
  test('UI-CHECKBOX-001 @smoke toggles checkboxes on and off', async ({ page }) => {
    await page.goto('/checkboxes');

    const checkboxes = page.locator('#checkboxes input[type="checkbox"]');
    const first = checkboxes.nth(0);
    const second = checkboxes.nth(1);

    await expect(checkboxes).toHaveCount(2);
    await expect(first).not.toBeChecked();
    await expect(second).toBeChecked();

    await first.check();
    await second.uncheck();

    await expect(first).toBeChecked();
    await expect(second).not.toBeChecked();
  });
});
