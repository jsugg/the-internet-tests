import { expect, test } from './fixtures.js';

test.describe('Dynamic Controls', () => {
  test('UI-DYNCTRL-001 removes and adds the checkbox', async ({ page }) => {
    await page.goto('/dynamic_controls');

    const checkbox = page.getByRole('checkbox');
    const message = page.locator('#message');

    await expect(checkbox).toHaveCount(1);
    await page.getByRole('button', { name: 'Remove' }).click();
    await expect(checkbox).toHaveCount(0);
    await expect(message).toHaveText("It's gone!");

    await page.getByRole('button', { name: 'Add' }).click();
    await expect(checkbox).toHaveCount(1);
    await expect(message).toHaveText("It's back!");
  });

  test('UI-DYNCTRL-002 toggles the checkbox state', async ({ page }) => {
    await page.goto('/dynamic_controls');

    const checkbox = page.getByRole('checkbox');

    await expect(checkbox).not.toBeChecked();
    await checkbox.check();
    await expect(checkbox).toBeChecked();
    await checkbox.uncheck();
    await expect(checkbox).not.toBeChecked();
  });

  test('UI-DYNCTRL-003 @smoke enables and disables the textbox', async ({ page }) => {
    await page.goto('/dynamic_controls');

    const textbox = page.getByRole('textbox');
    const message = page.locator('#message');

    await expect(textbox).toBeDisabled();
    await page.getByRole('button', { name: 'Enable' }).click();
    await expect(textbox).toBeEnabled();
    await expect(message).toHaveText("It's enabled!");

    await page.getByRole('button', { name: 'Disable' }).click();
    await expect(textbox).toBeDisabled();
    await expect(message).toHaveText("It's disabled!");
  });
});
