"""Redirect scenarios for the Python Playwright stack."""

from __future__ import annotations

import pytest
from playwright.sync_api import APIRequestContext

from the_internet_tests.config import AppConfig


@pytest.mark.http
def test_http_redirect_001_exposes_and_follows_redirect_behavior(
    api_request: APIRequestContext,
    app_config: AppConfig,
) -> None:
    """HTTP-REDIRECT-001 @http exposes and follows redirect behavior."""
    redirect = api_request.get("/redirect", max_redirects=0)

    assert redirect.status == 302
    assert redirect.headers["location"] == app_config.url("/status_codes")

    followed = api_request.get("/redirect")
    assert followed.status == 200
    assert "/status_codes" in followed.url
