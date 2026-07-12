import { expect, test } from './fixtures.js';

test.describe('Entry Ad', () => {
  test('UI-ENTRYAD-001 @flaky-demo closes the timed entry modal', async ({ page }) => {
    await page.goto('/entry_ad');

    const modal = page.locator('.modal');
    await expect(modal).toBeVisible();
    await expect(modal).toContainText('This is a modal window');

    await modal.getByText('Close').click();
    await expect(modal).toBeHidden();
  });
});
