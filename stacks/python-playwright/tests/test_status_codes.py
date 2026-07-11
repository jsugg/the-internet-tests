"""Status code scenarios for the Python Playwright stack."""

from __future__ import annotations

import pytest
from playwright.sync_api import APIRequestContext

STATUS_CODES = (200, 301, 404, 500)


@pytest.mark.http
def test_http_status_001_validates_status_code_endpoints(
    api_request: APIRequestContext,
) -> None:
    """HTTP-STATUS-001 @http validates status code endpoints."""
    for status_code in STATUS_CODES:
        response = api_request.get(
            f"/status_codes/{status_code}",
            fail_on_status_code=False,
            max_redirects=0,
        )

        assert response.status == status_code
