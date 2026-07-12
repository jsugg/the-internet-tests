import { expect, test } from './fixtures.js';

test.describe('Floating Menu', () => {
  test('UI-FLOATING-001 @desktop keeps the menu visible while scrolling', async ({ page }) => {
    await page.goto('/floating_menu');

    const menu = page.locator('#menu');
    await expect(menu).toBeVisible();

    await page.evaluate(() => window.scrollTo(0, document.body.scrollHeight));
    await expect(menu).toBeInViewport();
    await expect.poll(() => page.evaluate(() => window.scrollY)).toBeGreaterThan(0);
  });
});
