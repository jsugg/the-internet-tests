import { expect, test } from './fixtures.js';

const stableMenuItems = ['Home', 'About', 'Contact Us', 'Portfolio'] as const;
const allowedMenuItems = new Set<string>([...stableMenuItems, 'Gallery']);

test.describe('Disappearing Elements', () => {
  test('UI-DISAPPEAR-001 @flaky-demo tolerates the optional menu item', async ({ page }) => {
    await page.goto('/disappearing_elements');

    const menuItems = await page.locator('#content a').allTextContents();

    expect(menuItems).toEqual(expect.arrayContaining([...stableMenuItems]));
    expect(menuItems.every((item) => allowedMenuItems.has(item))).toBe(true);
  });
});
