"""Dropdown scenarios for the Python Playwright stack."""

from __future__ import annotations

import pytest
from playwright.sync_api import Page, expect


@pytest.mark.smoke
def test_ui_dropdown_001_selects_dropdown_options(page: Page) -> None:
    """UI-DROPDOWN-001 @smoke selects dropdown options."""
    page.goto("/dropdown")

    dropdown = page.locator("#dropdown")

    dropdown.select_option("1")
    expect(dropdown).to_have_value("1")
    expect(dropdown.locator("option:checked")).to_have_text("Option 1")

    dropdown.select_option("2")
    expect(dropdown).to_have_value("2")
    expect(dropdown.locator("option:checked")).to_have_text("Option 2")
