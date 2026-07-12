import { expect, test } from './fixtures.js';

const location = {
  latitude: 51.5,
  longitude: -0.12,
  accuracy: 10,
} as const;

test.describe('Geolocation', () => {
  test('UI-GEOLOCATION-001 @desktop displays the granted browser location', async ({
    context,
    page,
  }) => {
    await context.grantPermissions(['geolocation']);
    await context.setGeolocation(location);
    await page.goto('/geolocation');

    await page.getByRole('button', { name: 'Where am I?' }).click();

    await expect(page.locator('#lat-value')).toHaveText(String(location.latitude));
    await expect(page.locator('#long-value')).toHaveText(String(location.longitude));
    await expect(page.getByRole('link', { name: 'See it on Google' })).toHaveAttribute(
      'href',
      new RegExp(`${location.latitude}.*${location.longitude}`),
    );
  });
});
