"""Sortable table scenarios for the Python Playwright stack."""

from __future__ import annotations

from playwright.sync_api import Locator, Page, expect


def column_cells(page: Page, column: int) -> Locator:
    """Return cells for one zero-indexed table column."""
    return page.locator(f"#table1 tbody tr td:nth-child({column + 1})")


def test_ui_sorttable_001_sorts_table_rows_by_selected_columns(page: Page) -> None:
    """UI-SORTTABLE-001 sorts table rows by last name and due amount."""
    page.goto("/tables")

    page.locator("#table1 thead").get_by_text("Last Name").click()
    expect(column_cells(page, 0)).to_have_text(["Bach", "Conway", "Doe", "Smith"])

    page.locator("#table1 thead").get_by_text("Due").click()
    expect(column_cells(page, 3)).to_have_text(["$50.00", "$50.00", "$51.00", "$100.00"])
