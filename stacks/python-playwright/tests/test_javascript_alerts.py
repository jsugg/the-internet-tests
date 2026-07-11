"""JavaScript alert scenarios for the Python Playwright stack."""

from __future__ import annotations

from dataclasses import dataclass

import pytest
from playwright.sync_api import Dialog, Page, expect


@dataclass(frozen=True, slots=True)
class DialogExpectation:
    """Expected dialog metadata for a JavaScript alert control."""

    button_name: str
    message: str
    dialog_type: str


@dataclass(frozen=True, slots=True)
class DialogSnapshot:
    """Observed dialog metadata captured before handling the dialog."""

    message: str
    dialog_type: str


DIALOG_EXPECTATIONS = (
    DialogExpectation("Click for JS Alert", "I am a JS Alert", "alert"),
    DialogExpectation("Click for JS Confirm", "I am a JS Confirm", "confirm"),
    DialogExpectation("Click for JS Prompt", "I am a JS prompt", "prompt"),
)


def trigger_dialog(page: Page, button_name: str, prompt_text: str | None = None) -> DialogSnapshot:
    """Click a control and return the dialog snapshot it opens."""
    snapshots: list[DialogSnapshot] = []

    def handle_dialog(dialog: Dialog) -> None:
        snapshots.append(DialogSnapshot(message=dialog.message, dialog_type=dialog.type))
        if prompt_text is None:
            dialog.dismiss()
        else:
            dialog.accept(prompt_text)

    page.once("dialog", handle_dialog)
    page.get_by_role("button", name=button_name).click()
    if not snapshots:
        msg = f"button did not open a dialog: {button_name}"
        raise AssertionError(msg)
    return snapshots[0]


def test_ui_jsalert_001_opens_each_javascript_dialog_type(page: Page) -> None:
    """UI-JSALERT-001 opens each JavaScript dialog type."""
    page.goto("/javascript_alerts")

    for expected in DIALOG_EXPECTATIONS:
        dialog = trigger_dialog(page, expected.button_name)
        assert dialog.dialog_type == expected.dialog_type


def test_ui_jsalert_002_validates_javascript_dialog_messages(page: Page) -> None:
    """UI-JSALERT-002 validates JavaScript dialog messages."""
    page.goto("/javascript_alerts")

    for expected in DIALOG_EXPECTATIONS:
        dialog = trigger_dialog(page, expected.button_name)
        assert dialog.message == expected.message


@pytest.mark.smoke
def test_ui_jsalert_003_accepts_prompt_input(page: Page) -> None:
    """UI-JSALERT-003 @smoke accepts prompt input."""
    page.goto("/javascript_alerts")

    prompt_text = "This is a test message"
    dialog = trigger_dialog(page, "Click for JS Prompt", prompt_text)
    assert dialog.message == "I am a JS prompt"

    expect(page.locator("#result")).to_have_text(f"You entered: {prompt_text}")
