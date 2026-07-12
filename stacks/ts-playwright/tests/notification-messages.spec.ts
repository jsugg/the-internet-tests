import { expect, test } from './fixtures.js';

const expectedMessages = [
  'Action successful',
  'Action unsuccesful, please try again',
  'Action unsuccessful',
] as const;

test.describe('Notification Messages', () => {
  test('UI-NOTIFY-001 @desktop shows a recognized flash message', async ({ page }) => {
    await page.goto('/notification_message_rendered');

    await page.getByRole('link', { name: 'Click here' }).click();

    const flash = page.locator('#flash');
    await expect(flash).toBeVisible();
    const message = (await flash.textContent())?.replace('×', '').trim();

    expect(expectedMessages).toContain(message as (typeof expectedMessages)[number]);
  });
});
