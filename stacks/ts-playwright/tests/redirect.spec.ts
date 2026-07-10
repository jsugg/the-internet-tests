import { expect, test } from './fixtures.js';

test.describe('Redirect Link', () => {
  test('HTTP-REDIRECT-001 @http exposes and follows redirect behavior', async ({
    baseUrl,
    request,
  }) => {
    const redirect = await request.get('/redirect', { maxRedirects: 0 });

    expect(redirect.status()).toBe(302);
    expect(redirect.headers().location).toBe(`${baseUrl}/status_codes`);

    const followed = await request.get('/redirect');
    expect(followed.status()).toBe(200);
    expect(followed.url()).toContain('/status_codes');
  });
});
