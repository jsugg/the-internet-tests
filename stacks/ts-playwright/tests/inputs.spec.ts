import { expect, test } from './fixtures.js';

test.describe('Inputs', () => {
  test('UI-INPUT-001 accepts typed numeric input', async ({ page }) => {
    await page.goto('/inputs');

    const numberInput = page.locator('input[type="number"]');

    await numberInput.fill('12345');
    await expect(numberInput).toHaveValue('12345');

    await numberInput.press('ArrowDown');
    await expect(numberInput).toHaveValue('12344');

    await numberInput.fill('-7');
    await expect(numberInput).toHaveValue('-7');
  });
});
