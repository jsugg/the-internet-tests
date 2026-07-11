"""File upload scenarios for the Python Playwright stack."""

from __future__ import annotations

from pathlib import Path

import pytest
from playwright.sync_api import Page, expect

UPLOAD_FILE = (
    Path(__file__).resolve().parents[2]
    / "java-selenium-testng"
    / "src"
    / "test"
    / "resources"
    / "some-file.txt"
)


@pytest.mark.smoke
def test_ui_upload_001_shows_the_uploaded_file(page: Page) -> None:
    """UI-UPLOAD-001 @smoke shows the uploaded file."""
    page.goto("/upload")

    page.locator("#file-upload").set_input_files(UPLOAD_FILE)
    page.get_by_role("button", name="Upload").click()

    expect(page.get_by_role("heading", name="File Uploaded!")).to_be_visible()
    expect(page.locator("#uploaded-files")).to_have_text("some-file.txt")
