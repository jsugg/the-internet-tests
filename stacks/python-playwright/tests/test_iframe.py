"""iFrame scenarios for the Python Playwright stack."""

from __future__ import annotations

from playwright.sync_api import Page, expect


def test_ui_iframe_001_edits_content_inside_the_wysiwyg_frame(page: Page) -> None:
    """UI-IFRAME-001 edits content inside the WYSIWYG frame."""
    page.goto("/iframe")

    expect(
        page.get_by_role("heading", name="An iFrame containing the TinyMCE WYSIWYG Editor")
    ).to_be_visible()

    editor = page.frame_locator("#mce_0_ifr").locator("body#tinymce")
    expect(editor).to_contain_text("Your content goes here.")

    updated_text = "Written from Playwright"
    editor.evaluate("(body, text) => { body.textContent = text; }", updated_text)

    expect(editor).to_have_text(updated_text)
