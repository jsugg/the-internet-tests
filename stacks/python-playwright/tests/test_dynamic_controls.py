"""Dynamic controls scenarios for the Python Playwright stack."""

from __future__ import annotations

import pytest
from playwright.sync_api import Page, expect


def test_ui_dynctrl_001_removes_and_adds_the_checkbox(page: Page) -> None:
    """UI-DYNCTRL-001 removes and adds the checkbox."""
    page.goto("/dynamic_controls")

    checkbox = page.get_by_role("checkbox")
    message = page.locator("#message")

    expect(checkbox).to_have_count(1)
    page.get_by_role("button", name="Remove").click()
    expect(checkbox).to_have_count(0)
    expect(message).to_have_text("It's gone!")

    page.get_by_role("button", name="Add").click()
    expect(checkbox).to_have_count(1)
    expect(message).to_have_text("It's back!")


def test_ui_dynctrl_002_toggles_the_checkbox_state(page: Page) -> None:
    """UI-DYNCTRL-002 toggles the checkbox state."""
    page.goto("/dynamic_controls")

    checkbox = page.get_by_role("checkbox")

    expect(checkbox).not_to_be_checked()
    checkbox.check()
    expect(checkbox).to_be_checked()
    checkbox.uncheck()
    expect(checkbox).not_to_be_checked()


@pytest.mark.smoke
def test_ui_dynctrl_003_enables_and_disables_the_textbox(page: Page) -> None:
    """UI-DYNCTRL-003 @smoke enables and disables the textbox."""
    page.goto("/dynamic_controls")

    textbox = page.get_by_role("textbox")
    message = page.locator("#message")

    expect(textbox).to_be_disabled()
    page.get_by_role("button", name="Enable").click()
    expect(textbox).to_be_enabled()
    expect(message).to_have_text("It's enabled!")

    page.get_by_role("button", name="Disable").click()
    expect(textbox).to_be_disabled()
    expect(message).to_have_text("It's disabled!")
