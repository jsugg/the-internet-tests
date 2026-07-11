"""Login scenarios for the Python Playwright stack."""

from __future__ import annotations

import pytest
from playwright.sync_api import Page, expect

VALID_USERNAME = "tomsmith"
VALID_PASSWORD = "SuperSecretPassword!"


@pytest.mark.smoke
def test_ui_login_001_logs_in_with_valid_credentials(page: Page, app_base_url: str) -> None:
    """UI-LOGIN-001 @smoke logs in with valid credentials."""
    page.goto("/login")

    expect(page.get_by_role("heading", name="Login Page")).to_be_visible()
    page.get_by_label("Username").fill(VALID_USERNAME)
    page.get_by_label("Password").fill(VALID_PASSWORD)
    page.get_by_role("button", name="Login").click()

    expect(page).to_have_url(f"{app_base_url}/secure")
    expect(page.locator("#flash")).to_contain_text("You logged into a secure area!")


def test_ui_login_002_rejects_an_invalid_username(page: Page) -> None:
    """UI-LOGIN-002 rejects an invalid username."""
    page.goto("/login")

    page.get_by_label("Username").fill("invalidUsername")
    page.get_by_label("Password").fill(VALID_PASSWORD)
    page.get_by_role("button", name="Login").click()

    expect(page.locator("#flash")).to_contain_text("Your username is invalid!")


def test_ui_login_003_rejects_an_invalid_password(page: Page) -> None:
    """UI-LOGIN-003 rejects an invalid password."""
    page.goto("/login")

    page.get_by_label("Username").fill(VALID_USERNAME)
    page.get_by_label("Password").fill("invalidPassword")
    page.get_by_role("button", name="Login").click()

    expect(page.locator("#flash")).to_contain_text("Your password is invalid!")
