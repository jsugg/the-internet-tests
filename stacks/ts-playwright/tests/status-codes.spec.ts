import { expect, test } from './fixtures.js';

const statusCodes = [200, 301, 404, 500] as const;

test.describe('Status Codes', () => {
  test('HTTP-STATUS-001 @http validates status code endpoints', async ({ request }) => {
    for (const statusCode of statusCodes) {
      const response = await request.get(`/status_codes/${statusCode}`, {
        failOnStatusCode: false,
        maxRedirects: 0,
      });

      expect(response.status()).toBe(statusCode);
    }
  });
});
