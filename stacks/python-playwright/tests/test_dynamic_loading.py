"""Dynamic loading scenarios for the Python Playwright stack."""

from __future__ import annotations

import pytest
from playwright.sync_api import Page, expect


@pytest.mark.smoke
def test_ui_dynload_001_reveals_the_loaded_message(page: Page) -> None:
    """UI-DYNLOAD-001 @smoke reveals the loaded message."""
    page.goto("/dynamic_loading/2")

    expect(page.get_by_role("heading", name="Dynamically Loaded Page Elements")).to_be_visible()
    page.get_by_role("button", name="Start").click()

    expect(page.locator("#finish")).to_have_text("Hello World!", timeout=15_000)
