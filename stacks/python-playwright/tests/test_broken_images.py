"""Broken image scenarios for the Python Playwright stack."""

from __future__ import annotations

from urllib.parse import urljoin

from playwright.sync_api import APIRequestContext, Page

from the_internet_tests.config import AppConfig


def test_ui_brokenimg_001_validates_broken_and_loaded_image_resources(
    page: Page,
    api_request: APIRequestContext,
    app_config: AppConfig,
) -> None:
    """UI-BROKENIMG-001 validates broken and loaded image resources."""
    page.goto("/broken_images")

    raw_sources = page.locator(".example img").evaluate_all(
        "images => images.map(image => image.getAttribute('src'))"
    )
    if not isinstance(raw_sources, list):
        raise AssertionError("broken image scenario expected a list of image sources")

    image_sources: list[str] = []
    for raw_source in raw_sources:
        if not isinstance(raw_source, str) or not raw_source:
            raise AssertionError("broken image scenario expected every image to have a src")
        image_sources.append(raw_source)

    statuses = [
        api_request.get(urljoin(f"{app_config.base_url}/", src), fail_on_status_code=False).status
        for src in image_sources
    ]

    assert statuses == [404, 404, 200]
