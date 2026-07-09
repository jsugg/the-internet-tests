import { expect, test } from './fixtures.js';

const validUser = {
  username: 'tomsmith',
  password: 'SuperSecretPassword!',
} as const;

test.describe('Login', () => {
  test('UI-LOGIN-001 @smoke logs in with valid credentials', async ({ baseUrl, page }) => {
    await page.goto('/login');

    await expect(page.getByRole('heading', { name: 'Login Page' })).toBeVisible();
    await page.getByLabel('Username').fill(validUser.username);
    await page.getByLabel('Password').fill(validUser.password);
    await page.getByRole('button', { name: 'Login' }).click();

    await expect(page).toHaveURL(`${baseUrl}/secure`);
    await expect(page.locator('#flash')).toContainText('You logged into a secure area!');
  });
});
