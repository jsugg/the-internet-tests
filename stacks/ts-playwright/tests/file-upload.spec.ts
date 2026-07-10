import { dirname, resolve } from 'node:path';
import { fileURLToPath } from 'node:url';

import { expect, test } from './fixtures.js';

const uploadFile = resolve(
  dirname(fileURLToPath(import.meta.url)),
  '../../java-selenium-testng/src/test/resources/some-file.txt',
);

test.describe('File Upload', () => {
  test('UI-UPLOAD-001 @smoke shows the uploaded file', async ({ page }) => {
    await page.goto('/upload');

    await page.locator('#file-upload').setInputFiles(uploadFile);
    await page.getByRole('button', { name: 'Upload' }).click();

    await expect(page.getByRole('heading', { name: 'File Uploaded!' })).toBeVisible();
    await expect(page.locator('#uploaded-files')).toHaveText('some-file.txt');
  });
});
