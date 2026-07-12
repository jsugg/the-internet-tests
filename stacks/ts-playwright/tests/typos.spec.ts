import { expect, test } from './fixtures.js';

test.describe('Typos', () => {
  test('UI-TYPOS-001 @flaky-demo documents nondeterministic copy', async ({ page }) => {
    await page.goto('/typos');

    const paragraphs = page.locator('.example p');
    await expect(paragraphs.first()).toContainText('This example demonstrates a typo');

    const variant = (await paragraphs.nth(1).textContent())?.trim() ?? '';
    expect(variant).toMatch(/Sometimes you'll see a typo, other times you (won't|won,t)\./);
  });
});
