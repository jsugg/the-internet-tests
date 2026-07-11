"""Input scenarios for the Python Playwright stack."""

from __future__ import annotations

from playwright.sync_api import Page, expect


def test_ui_input_001_accepts_typed_numeric_input(page: Page) -> None:
    """UI-INPUT-001 accepts typed numeric input."""
    page.goto("/inputs")

    number_input = page.locator('input[type="number"]')

    number_input.fill("12345")
    expect(number_input).to_have_value("12345")

    number_input.press("ArrowDown")
    expect(number_input).to_have_value("12344")

    number_input.fill("-7")
    expect(number_input).to_have_value("-7")
