import { expect, test } from './fixtures.js';

test.describe('Exit Intent', () => {
  test('UI-EXITINTENT-001 @desktop @not-ci opens the exit modal on mouse leave', async ({
    page,
  }) => {
    await page.goto('/exit_intent');

    await page.mouse.move(300, 300);
    await page.mouse.move(300, -10);

    const modal = page.locator('.modal');
    await expect(modal).toBeVisible();
    await expect(modal).toContainText('This is a modal window');
  });
});
