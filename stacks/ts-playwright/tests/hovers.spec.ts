import { expect, test } from './fixtures.js';

test.describe('Hovers', () => {
  test('UI-HOVERS-001 @desktop reveals user details on hover', async ({ page }) => {
    await page.goto('/hovers');

    const figures = page.locator('.figure');
    await expect(figures).toHaveCount(3);

    for (let index = 0; index < 3; index += 1) {
      const figure = figures.nth(index);
      const caption = figure.locator('.figcaption');
      const userNumber = index + 1;

      await expect(caption).toBeHidden();
      await figure.hover();
      await expect(caption).toContainText(`name: user${userNumber}`);
      await expect(caption.getByRole('link', { name: 'View profile' })).toHaveAttribute(
        'href',
        `/users/${userNumber}`,
      );
    }
  });
});
