import { readFile } from 'node:fs/promises';
import { dirname, resolve } from 'node:path';
import { fileURLToPath } from 'node:url';

import { expect, test } from './fixtures.js';

const downloadFileName = 'some-file.txt';
const expectedFilePath = resolve(
  dirname(fileURLToPath(import.meta.url)),
  '../../java-selenium-testng/src/test/resources/some-file.txt',
);

test.describe('File Download', () => {
  test('UI-DOWNLOAD-001 @desktop downloads the expected file through the browser', async ({
    page,
  }) => {
    await page.goto('/download');

    const downloadPromise = page.waitForEvent('download');
    await page.getByRole('link', { name: downloadFileName }).click();
    const download = await downloadPromise;

    expect(download.suggestedFilename()).toBe(downloadFileName);
    const downloadedPath = await download.path();
    if (!downloadedPath) {
      throw new Error('Expected Playwright to expose the downloaded file path.');
    }
    const [expected, actual] = await Promise.all([
      readFile(expectedFilePath, 'utf8'),
      readFile(downloadedPath, 'utf8'),
    ]);

    expect(actual).toBe(expected);
  });
});
