import { createHash } from 'node:crypto';
import { readFile } from 'node:fs/promises';
import { dirname, resolve } from 'node:path';
import { fileURLToPath } from 'node:url';

import { request as requestFactory, type APIRequestContext } from '@playwright/test';

import { expect, test } from './fixtures.js';

const AUTH_CREDENTIALS = {
  username: 'admin',
  password: 'admin',
} as const;
const AUTH_SUCCESS_TEXT = 'Congratulations! You must have the proper credentials.';
const DIGEST_URI = '/digest_auth';
const DOWNLOAD_FILE_NAME = 'some-file.txt';
const DOWNLOAD_FILE_PATH = resolve(
  dirname(fileURLToPath(import.meta.url)),
  '../../java-selenium-testng/src/test/resources/some-file.txt',
);
const RESOURCE_TIMEOUT_MS = 500;

type DigestChallenge = {
  nonce: string;
  opaque: string;
  qop: 'auth';
  realm: string;
};

function md5(value: string): string {
  return createHash('md5').update(value).digest('hex');
}

function requireDigestValue(fields: Record<string, string>, key: string): string {
  const value = fields[key];
  if (!value) {
    throw new Error(`Digest challenge is missing ${key}.`);
  }

  return value;
}

function parseDigestChallenge(header: string | undefined): DigestChallenge {
  if (!header?.startsWith('Digest ')) {
    throw new Error('Digest challenge did not include a Digest WWW-Authenticate header.');
  }

  const fields: Record<string, string> = {};
  for (const match of header.slice('Digest '.length).matchAll(/(\w+)=(?:"([^"]*)"|([^,]+))/g)) {
    const key = match[1];
    const value = match[2] ?? match[3];
    if (!key || !value) {
      throw new Error(`Digest challenge contains an invalid field: ${match[0]}`);
    }

    fields[key] = value.trim();
  }

  const qop = requireDigestValue(fields, 'qop');
  if (qop !== 'auth') {
    throw new Error(`Digest challenge qop ${qop} is unsupported.`);
  }

  return {
    nonce: requireDigestValue(fields, 'nonce'),
    opaque: requireDigestValue(fields, 'opaque'),
    qop,
    realm: requireDigestValue(fields, 'realm'),
  };
}

function digestAuthorizationHeader(challenge: DigestChallenge): string {
  const method = 'GET';
  const nc = '00000001';
  const cnonce = 'the-internet-tests';
  const ha1 = md5(`${AUTH_CREDENTIALS.username}:${challenge.realm}:${AUTH_CREDENTIALS.password}`);
  const ha2 = md5(`${method}:${DIGEST_URI}`);
  const response = md5(`${ha1}:${challenge.nonce}:${nc}:${cnonce}:${challenge.qop}:${ha2}`);

  return [
    `Digest username="${AUTH_CREDENTIALS.username}"`,
    `realm="${challenge.realm}"`,
    `nonce="${challenge.nonce}"`,
    `uri="${DIGEST_URI}"`,
    `qop=${challenge.qop}`,
    `nc=${nc}`,
    `cnonce="${cnonce}"`,
    `response="${response}"`,
    `opaque="${challenge.opaque}"`,
  ].join(', ');
}

async function authenticatedRequest(baseUrl: string): Promise<APIRequestContext> {
  return requestFactory.newContext({
    baseURL: baseUrl,
    httpCredentials: AUTH_CREDENTIALS,
  });
}

test.describe('HTTP resources', () => {
  test('HTTP-BASICAUTH-001 @http validates basic authentication', async ({ baseUrl, request }) => {
    const unauthenticated = await request.get('/basic_auth', { failOnStatusCode: false });

    expect(unauthenticated.status()).toBe(401);
    expect(unauthenticated.headers()['www-authenticate']).toContain('Basic');

    const authRequest = await authenticatedRequest(baseUrl);
    try {
      const authenticated = await authRequest.get('/basic_auth');

      expect(authenticated.status()).toBe(200);
      await expect(authenticated.text()).resolves.toContain(AUTH_SUCCESS_TEXT);
    } finally {
      await authRequest.dispose();
    }
  });

  test('HTTP-DIGESTAUTH-001 @http validates digest authentication', async ({ request }) => {
    const challengeResponse = await request.get(DIGEST_URI, { failOnStatusCode: false });

    expect(challengeResponse.status()).toBe(401);

    const challenge = parseDigestChallenge(challengeResponse.headers()['www-authenticate']);
    const authenticated = await request.get(DIGEST_URI, {
      failOnStatusCode: false,
      headers: {
        Authorization: digestAuthorizationHeader(challenge),
      },
    });

    expect(authenticated.status()).toBe(200);
    await expect(authenticated.text()).resolves.toContain(AUTH_SUCCESS_TEXT);
  });

  test('UI-DOWNLOAD-001 @http downloads the expected file directly', async ({ request }) => {
    const [expectedFile, download] = await Promise.all([
      readFile(DOWNLOAD_FILE_PATH, 'utf8'),
      request.get(`/download/${DOWNLOAD_FILE_NAME}`),
    ]);

    expect(download.status()).toBe(200);
    expect(download.headers()['content-disposition']).toBe(
      `attachment; filename="${DOWNLOAD_FILE_NAME}"`,
    );
    await expect(download.text()).resolves.toBe(expectedFile);
  });

  test('HTTP-SLOW-001 @http fails slow resources with a bounded timeout', async ({ request }) => {
    const start = Date.now();

    await expect(request.get('/slow_external', { timeout: RESOURCE_TIMEOUT_MS })).rejects.toThrow(
      /Request timed out|Timeout/,
    );

    expect(Date.now() - start).toBeLessThan(5_000);
  });
});
