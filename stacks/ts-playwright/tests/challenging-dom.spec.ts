import { expect, test } from './fixtures.js';

test.describe('Challenging DOM', () => {
  test('UI-CHALLENGING-001 @desktop validates stable content without dynamic ids', async ({
    page,
  }) => {
    await page.goto('/challenging_dom');

    await expect(page.getByRole('heading', { name: 'Challenging DOM' })).toBeVisible();
    await expect(page.locator('.button')).toHaveCount(3);
    await expect(page.locator('canvas#canvas')).toBeVisible();
    await expect(page.locator('table thead th')).toHaveText([
      'Lorem',
      'Ipsum',
      'Dolor',
      'Sit',
      'Amet',
      'Diceret',
      'Action',
    ]);
    await expect(page.locator('table tbody tr')).toHaveCount(10);
    await expect(page.locator('table tbody tr').first()).toContainText('Iuvaret0');
    await expect(page.getByRole('link', { name: 'edit' }).first()).toHaveAttribute('href', '#edit');
    await expect(page.getByRole('link', { name: 'delete' }).first()).toHaveAttribute(
      'href',
      '#delete',
    );
  });
});
