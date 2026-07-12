import { expect, test } from './fixtures.js';

test.describe('Context Menu', () => {
  test('UI-CONTEXT-001 @desktop opens the expected alert', async ({ page }) => {
    await page.goto('/context_menu');

    page.once('dialog', async (dialog) => {
      expect(dialog.message()).toBe('You selected a context menu');
      await dialog.accept();
    });

    await page.locator('#hot-spot').click({ button: 'right' });
  });
});
