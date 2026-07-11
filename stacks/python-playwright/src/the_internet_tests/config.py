"""Runtime configuration for Python Playwright tests."""

from __future__ import annotations

import os
from dataclasses import dataclass
from urllib.parse import urljoin

DEFAULT_BASE_URL = "http://localhost:7080"
BASE_URL_ENV = "THE_INTERNET_BASE_URL"


@dataclass(frozen=True, slots=True)
class AppConfig:
    """Validated The Internet app configuration."""

    base_url: str

    def url(self, path: str) -> str:
        """Return an absolute application URL for a route path."""
        if not path.startswith("/"):
            msg = f"route path must start with '/': {path}"
            raise ValueError(msg)
        return urljoin(f"{self.base_url}/", path.removeprefix("/"))


def load_config() -> AppConfig:
    """Load test configuration from environment variables."""
    raw_base_url = os.environ.get(BASE_URL_ENV, DEFAULT_BASE_URL).strip()
    if not raw_base_url:
        msg = f"{BASE_URL_ENV} must not be empty."
        raise ValueError(msg)
    return AppConfig(base_url=raw_base_url.rstrip("/"))
