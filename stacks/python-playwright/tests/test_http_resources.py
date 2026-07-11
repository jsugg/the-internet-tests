"""HTTP/resource scenarios for the Python Playwright stack."""

from __future__ import annotations

import re
import time
from collections.abc import Generator
from dataclasses import dataclass
from hashlib import md5 as md5_factory
from pathlib import Path
from typing import Literal

import pytest
from playwright.sync_api import (
    APIRequestContext,
    HttpCredentials,
    Playwright,
)
from playwright.sync_api import (
    TimeoutError as PlaywrightTimeoutError,
)

AUTH_CREDENTIALS: HttpCredentials = {"username": "admin", "password": "admin"}
AUTH_SUCCESS_TEXT = "Congratulations! You must have the proper credentials."
DIGEST_URI = "/digest_auth"
DOWNLOAD_FILE_NAME = "some-file.txt"
DOWNLOAD_FILE_PATH = (
    Path(__file__).resolve().parents[2]
    / "java-selenium-testng"
    / "src"
    / "test"
    / "resources"
    / DOWNLOAD_FILE_NAME
)
RESOURCE_TIMEOUT_MS = 500


@dataclass(frozen=True, slots=True)
class DigestChallenge:
    """Digest authentication challenge fields supported by this test."""

    nonce: str
    opaque: str
    qop: Literal["auth"]
    realm: str


def md5(value: str) -> str:
    """Return the MD5 digest required by RFC 7616 response calculation."""
    return md5_factory(value.encode()).hexdigest()


def require_digest_value(fields: dict[str, str], key: str) -> str:
    """Return a required digest challenge value."""
    value = fields.get(key)
    if not value:
        msg = f"Digest challenge is missing {key}."
        raise ValueError(msg)
    return value


def parse_digest_challenge(header: str | None) -> DigestChallenge:
    """Parse the WWW-Authenticate digest challenge."""
    if header is None or not header.startswith("Digest "):
        msg = "Digest challenge did not include a Digest WWW-Authenticate header."
        raise ValueError(msg)

    fields: dict[str, str] = {}
    for match in re.finditer(r'(\w+)=(?:"([^"]*)"|([^,]+))', header.removeprefix("Digest ")):
        key = match.group(1)
        value = match.group(2) or match.group(3)
        if not value:
            msg = f"Digest challenge contains an invalid field: {match.group(0)}"
            raise ValueError(msg)
        fields[key] = value.strip()

    qop = require_digest_value(fields, "qop")
    if qop != "auth":
        msg = f"Digest challenge qop {qop} is unsupported."
        raise ValueError(msg)

    return DigestChallenge(
        nonce=require_digest_value(fields, "nonce"),
        opaque=require_digest_value(fields, "opaque"),
        qop="auth",
        realm=require_digest_value(fields, "realm"),
    )


def digest_authorization_header(challenge: DigestChallenge) -> str:
    """Build a Digest authorization header for The Internet test credentials."""
    method = "GET"
    nc = "00000001"
    cnonce = "the-internet-tests"
    ha1 = md5(f"{AUTH_CREDENTIALS['username']}:{challenge.realm}:{AUTH_CREDENTIALS['password']}")
    ha2 = md5(f"{method}:{DIGEST_URI}")
    response = md5(f"{ha1}:{challenge.nonce}:{nc}:{cnonce}:{challenge.qop}:{ha2}")

    return ", ".join(
        [
            f'Digest username="{AUTH_CREDENTIALS["username"]}"',
            f'realm="{challenge.realm}"',
            f'nonce="{challenge.nonce}"',
            f'uri="{DIGEST_URI}"',
            f"qop={challenge.qop}",
            f"nc={nc}",
            f'cnonce="{cnonce}"',
            f'response="{response}"',
            f'opaque="{challenge.opaque}"',
        ]
    )


@pytest.fixture
def authenticated_api_request(
    playwright: Playwright,
    app_base_url: str,
) -> Generator[APIRequestContext]:
    """Return an API request context with basic-auth credentials."""
    request_context = playwright.request.new_context(
        base_url=app_base_url,
        http_credentials=AUTH_CREDENTIALS,
    )
    try:
        yield request_context
    finally:
        request_context.dispose()


@pytest.mark.http
def test_http_basicauth_001_validates_basic_authentication(
    api_request: APIRequestContext,
    authenticated_api_request: APIRequestContext,
) -> None:
    """HTTP-BASICAUTH-001 @http validates basic authentication."""
    unauthenticated = api_request.get("/basic_auth", fail_on_status_code=False)

    assert unauthenticated.status == 401
    assert "Basic" in unauthenticated.headers["www-authenticate"]

    authenticated = authenticated_api_request.get("/basic_auth")
    assert authenticated.status == 200
    assert AUTH_SUCCESS_TEXT in authenticated.text()


@pytest.mark.http
def test_http_digestauth_001_validates_digest_authentication(
    api_request: APIRequestContext,
) -> None:
    """HTTP-DIGESTAUTH-001 @http validates digest authentication."""
    challenge_response = api_request.get(DIGEST_URI, fail_on_status_code=False)

    assert challenge_response.status == 401

    challenge = parse_digest_challenge(challenge_response.headers.get("www-authenticate"))
    authenticated = api_request.get(
        DIGEST_URI,
        fail_on_status_code=False,
        headers={"Authorization": digest_authorization_header(challenge)},
    )

    assert authenticated.status == 200
    assert AUTH_SUCCESS_TEXT in authenticated.text()


@pytest.mark.http
def test_ui_download_001_downloads_the_expected_file_directly(
    api_request: APIRequestContext,
) -> None:
    """UI-DOWNLOAD-001 @http downloads the expected file directly."""
    expected_file = DOWNLOAD_FILE_PATH.read_text()
    download = api_request.get(f"/download/{DOWNLOAD_FILE_NAME}")

    assert download.status == 200
    assert download.headers["content-disposition"] == f'attachment; filename="{DOWNLOAD_FILE_NAME}"'
    assert download.text() == expected_file


@pytest.mark.http
def test_http_slow_001_fails_slow_resources_with_a_bounded_timeout(
    api_request: APIRequestContext,
) -> None:
    """HTTP-SLOW-001 @http fails slow resources with a bounded timeout."""
    start = time.monotonic()

    with pytest.raises(PlaywrightTimeoutError, match="Timeout"):
        api_request.get("/slow_external", timeout=RESOURCE_TIMEOUT_MS)

    assert time.monotonic() - start < 5
