"""Add/remove element scenarios for the Python Playwright stack."""

from __future__ import annotations

from playwright.sync_api import Page, expect


def test_ui_addremove_001_adds_and_removes_delete_buttons(page: Page) -> None:
    """UI-ADDREMOVE-001 adds and removes delete buttons."""
    page.goto("/add_remove_elements/")

    add_button = page.get_by_role("button", name="Add Element")
    delete_buttons = page.get_by_role("button", name="Delete")

    expect(delete_buttons).to_have_count(0)
    add_button.click()
    add_button.click()
    add_button.click()
    expect(delete_buttons).to_have_count(3)

    delete_buttons.nth(1).click()
    expect(delete_buttons).to_have_count(2)
