import { expect, test } from './fixtures.js';

test.describe('Shadow DOM', () => {
  test('UI-SHADOWDOM-001 @desktop reads text through open shadow roots and slots', async ({
    page,
  }) => {
    await page.goto('/shadowdom');

    await expect(page.locator('my-paragraph').first().locator('span')).toHaveText(
      "Let's have some different text!",
    );
    await expect(page.locator('my-paragraph').nth(1)).toContainText(
      "Let's have some different text!",
    );
    await expect(page.locator('my-paragraph').nth(1)).toContainText('In a list!');
    await expect(page.locator('my-paragraph').nth(1)).toContainText('My default text');
  });
});
