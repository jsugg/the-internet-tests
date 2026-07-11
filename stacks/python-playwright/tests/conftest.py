"""Pytest fixtures shared by the Python Playwright stack."""

from __future__ import annotations

from collections.abc import Mapping

import pytest

from the_internet_tests.config import AppConfig, load_config


@pytest.fixture(scope="session")
def app_config() -> AppConfig:
    """Return validated app configuration for tests."""
    return load_config()


@pytest.fixture(scope="session")
def app_base_url(app_config: AppConfig) -> str:
    """Return The Internet base URL without a trailing slash."""
    return app_config.base_url


@pytest.fixture(scope="session")
def browser_context_args(
    browser_context_args: Mapping[str, object],
    app_config: AppConfig,
) -> dict[str, object]:
    """Configure Playwright browser contexts with the app base URL."""
    return {**browser_context_args, "base_url": app_config.base_url}
