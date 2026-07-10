import { expect, test } from './fixtures.js';

test.describe('Dynamic Loading', () => {
  test('UI-DYNLOAD-001 @smoke reveals the loaded message', async ({ page }) => {
    await page.goto('/dynamic_loading/2');

    await expect(
      page.getByRole('heading', { name: 'Dynamically Loaded Page Elements' }),
    ).toBeVisible();
    await page.getByRole('button', { name: 'Start' }).click();

    await expect(page.locator('#finish')).toHaveText('Hello World!', { timeout: 15_000 });
  });
});
