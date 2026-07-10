import { expect, test } from './fixtures.js';

test.describe('Broken Images', () => {
  test('UI-BROKENIMG-001 validates broken and loaded image resources', async ({
    baseUrl,
    page,
    request,
  }) => {
    await page.goto('/broken_images');

    const imageSources = await page.locator('.example img').evaluateAll((images) =>
      images.map((image) => {
        const src = image.getAttribute('src');
        if (!src) {
          throw new Error('Broken image scenario expected every image to have a src.');
        }
        return src;
      }),
    );

    const statuses = [];
    for (const src of imageSources) {
      const response = await request.get(new URL(src, baseUrl).toString(), {
        failOnStatusCode: false,
      });
      statuses.push(response.status());
    }

    expect(statuses).toEqual([404, 404, 200]);
  });
});
