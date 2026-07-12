import { expect, test } from './fixtures.js';

test.describe('Horizontal Slider', () => {
  test('UI-SLIDER-001 @desktop moves the slider by keyboard increments', async ({ page }) => {
    await page.goto('/horizontal_slider');

    const slider = page.locator('input[type="range"]');
    await slider.focus();
    for (let step = 0; step < 8; step += 1) {
      await slider.press('ArrowRight');
    }

    await expect(page.locator('#range')).toHaveText('4');
    await expect(slider).toHaveValue('4');
  });
});
