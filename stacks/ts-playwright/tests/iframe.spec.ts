import { expect, test } from './fixtures.js';

test.describe('iFrame', () => {
  test('UI-IFRAME-001 edits content inside the WYSIWYG frame', async ({ page }) => {
    await page.goto('/iframe');

    await expect(
      page.getByRole('heading', { name: 'An iFrame containing the TinyMCE WYSIWYG Editor' }),
    ).toBeVisible();

    const editor = page.frameLocator('#mce_0_ifr').locator('body#tinymce');
    await expect(editor).toContainText('Your content goes here.');

    const updatedText = 'Written from Playwright';
    await editor.evaluate((body, text) => {
      body.textContent = text;
    }, updatedText);

    await expect(editor).toHaveText(updatedText);
  });
});
