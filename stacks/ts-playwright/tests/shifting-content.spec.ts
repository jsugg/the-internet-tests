import { expect, test } from './fixtures.js';

test.describe('Shifting Content', () => {
  test('UI-SHIFTING-001 @flaky-demo documents randomized menu placement', async ({ page }) => {
    await page.goto('/shifting_content/menu?mode=random&pixel_shift=100');

    const gallery = page.getByRole('link', { name: 'Gallery' });
    await expect(gallery).toHaveAttribute('href', '/gallery/');

    const className = await gallery.getAttribute('class');
    expect([null, 'shift']).toContain(className);
  });
});
