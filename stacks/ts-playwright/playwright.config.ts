import { defineConfig, devices } from '@playwright/test';

const baseURL = process.env.THE_INTERNET_BASE_URL ?? 'http://localhost:7080';
const isCI = Boolean(process.env.CI);
const htmlReportOutputFolder = process.env.PLAYWRIGHT_HTML_REPORT ?? 'playwright-report';
const testResultsOutputDir = process.env.PLAYWRIGHT_TEST_RESULTS ?? './test-results';

export default defineConfig({
  testDir: './tests',
  outputDir: testResultsOutputDir,
  fullyParallel: true,
  forbidOnly: isCI,
  retries: isCI ? 1 : 0,
  ...(isCI ? { workers: 1 } : {}),
  reporter: isCI
    ? [['list'], ['html', { open: 'never', outputFolder: htmlReportOutputFolder }]]
    : 'list',
  use: {
    baseURL,
    screenshot: 'only-on-failure',
    trace: isCI ? 'on-first-retry' : 'retain-on-failure',
    video: 'retain-on-failure',
  },
  projects: [
    { name: 'chromium', use: { ...devices['Desktop Chrome'] } },
    { name: 'firefox', use: { ...devices['Desktop Firefox'] } },
    { name: 'webkit', use: { ...devices['Desktop Safari'] } },
    { name: 'chrome', use: { ...devices['Desktop Chrome'], channel: 'chrome' } },
    { name: 'msedge', use: { ...devices['Desktop Edge'], channel: 'msedge' } },
    { name: 'Mobile Chrome', use: { ...devices['Pixel 5'] } },
    { name: 'Mobile Safari', use: { ...devices['iPhone 12'] } },
  ],
});
