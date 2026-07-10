import { expect, test } from './fixtures.js';

test.describe('Dropdown', () => {
  test('UI-DROPDOWN-001 @smoke selects dropdown options', async ({ page }) => {
    await page.goto('/dropdown');

    const dropdown = page.locator('#dropdown');

    await dropdown.selectOption('1');
    await expect(dropdown).toHaveValue('1');
    await expect(dropdown.locator('option:checked')).toHaveText('Option 1');

    await dropdown.selectOption('2');
    await expect(dropdown).toHaveValue('2');
    await expect(dropdown.locator('option:checked')).toHaveText('Option 2');
  });
});
