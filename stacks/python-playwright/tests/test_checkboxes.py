"""Checkbox scenarios for the Python Playwright stack."""

from __future__ import annotations

import pytest
from playwright.sync_api import Page, expect


@pytest.mark.smoke
def test_ui_checkbox_001_toggles_checkboxes_on_and_off(page: Page) -> None:
    """UI-CHECKBOX-001 @smoke toggles checkboxes on and off."""
    page.goto("/checkboxes")

    checkboxes = page.locator('#checkboxes input[type="checkbox"]')
    first = checkboxes.nth(0)
    second = checkboxes.nth(1)

    expect(checkboxes).to_have_count(2)
    expect(first).not_to_be_checked()
    expect(second).to_be_checked()

    first.check()
    second.uncheck()

    expect(first).to_be_checked()
    expect(second).not_to_be_checked()
