"""Multiple-window scenarios for the Python Playwright stack."""

from __future__ import annotations

from playwright.sync_api import Page, expect


def test_ui_windows_001_opens_expected_content_in_a_new_tab(page: Page) -> None:
    """UI-WINDOWS-001 opens expected content in a new tab."""
    page.goto("/windows")

    with page.expect_popup() as popup_info:
        page.get_by_role("link", name="Click Here").click()
    popup = popup_info.value

    expect(popup.get_by_role("heading", name="New Window")).to_be_visible()
    popup.close()
