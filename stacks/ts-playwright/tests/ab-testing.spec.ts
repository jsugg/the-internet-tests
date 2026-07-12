import { expect, test } from './fixtures.js';

test.describe('A/B Testing', () => {
  test('UI-ABTEST-001 @flaky-demo accepts either experiment variant', async ({ page }) => {
    await page.goto('/abtest');

    await expect(page.getByRole('heading')).toHaveText(/A\/B Test/);
    await expect(page.locator('.example p')).toContainText('Also known as split testing');
  });
});
