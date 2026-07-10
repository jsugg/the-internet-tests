import type { Dialog, Page } from '@playwright/test';

import { expect, test } from './fixtures.js';

type DialogExpectation = Readonly<{
  buttonName: string;
  message: string;
  type: ReturnType<Dialog['type']>;
}>;

type DialogSnapshot = Readonly<{
  message: string;
  type: ReturnType<Dialog['type']>;
}>;

const dialogExpectations: readonly DialogExpectation[] = [
  { buttonName: 'Click for JS Alert', message: 'I am a JS Alert', type: 'alert' },
  { buttonName: 'Click for JS Confirm', message: 'I am a JS Confirm', type: 'confirm' },
  { buttonName: 'Click for JS Prompt', message: 'I am a JS prompt', type: 'prompt' },
];

async function triggerDialog(
  page: Page,
  buttonName: string,
  promptText?: string,
): Promise<DialogSnapshot> {
  const dialogSnapshot = new Promise<DialogSnapshot>((resolve, reject) => {
    page.once('dialog', (dialog) => {
      const snapshot: DialogSnapshot = {
        message: dialog.message(),
        type: dialog.type(),
      };
      const handler = promptText === undefined ? dialog.dismiss() : dialog.accept(promptText);
      handler.then(() => resolve(snapshot), reject);
    });
  });

  await page.getByRole('button', { name: buttonName }).click();
  return dialogSnapshot;
}

test.describe('JavaScript Alerts', () => {
  test('UI-JSALERT-001 opens each JavaScript dialog type', async ({ page }) => {
    await page.goto('/javascript_alerts');

    for (const expected of dialogExpectations) {
      const dialog = await triggerDialog(page, expected.buttonName);
      expect(dialog.type).toBe(expected.type);
    }
  });

  test('UI-JSALERT-002 validates JavaScript dialog messages', async ({ page }) => {
    await page.goto('/javascript_alerts');

    for (const expected of dialogExpectations) {
      const dialog = await triggerDialog(page, expected.buttonName);
      expect(dialog.message).toBe(expected.message);
    }
  });

  test('UI-JSALERT-003 @smoke accepts prompt input', async ({ page }) => {
    await page.goto('/javascript_alerts');

    const promptText = 'This is a test message';
    const dialog = await triggerDialog(page, 'Click for JS Prompt', promptText);
    expect(dialog.message).toBe('I am a JS prompt');

    await expect(page.locator('#result')).toHaveText(`You entered: ${promptText}`);
  });
});
