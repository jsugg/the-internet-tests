import { expect, test } from './fixtures.js';

test.describe('Infinite Scroll', () => {
  test('UI-INFINITE-001 @desktop loads additional content while scrolling', async ({ page }) => {
    await page.goto('/infinite_scroll');

    const paragraphs = page.locator('.jscroll-added');
    await expect.poll(() => paragraphs.count()).toBeGreaterThanOrEqual(1);
    const initialCount = await paragraphs.count();

    await page.evaluate(() => window.scrollTo(0, document.body.scrollHeight));

    await expect.poll(() => paragraphs.count()).toBeGreaterThan(initialCount);
    await expect(paragraphs.last()).toBeVisible();
  });
});
