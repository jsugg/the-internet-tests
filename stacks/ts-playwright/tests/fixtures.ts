import { expect, test as base } from '@playwright/test';

type AppFixtures = {
  baseUrl: string;
};

export const test = base.extend<AppFixtures>({
  baseUrl: async ({ baseURL }, use) => {
    if (!baseURL) {
      throw new Error('Playwright baseURL must be configured before tests run.');
    }

    await use(baseURL.replace(/\/$/, ''));
  },
});

export { expect };
