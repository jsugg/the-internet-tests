import { expect, test } from './fixtures.js';

test.describe('Add/Remove Elements', () => {
  test('UI-ADDREMOVE-001 adds and removes delete buttons', async ({ page }) => {
    await page.goto('/add_remove_elements/');

    const addButton = page.getByRole('button', { name: 'Add Element' });
    const deleteButtons = page.getByRole('button', { name: 'Delete' });

    await expect(deleteButtons).toHaveCount(0);
    await addButton.click();
    await addButton.click();
    await addButton.click();
    await expect(deleteButtons).toHaveCount(3);

    await deleteButtons.nth(1).click();
    await expect(deleteButtons).toHaveCount(2);
  });
});
