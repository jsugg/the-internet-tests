package theinternetwebsite.ui.driver;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

public final class DriverFactory {
    public static final String DOWNLOADS_FOLDER = Paths.get("target/").toAbsolutePath().toString();

    public @NotNull RemoteWebDriver create(@NotNull TestRunConfig config) {
        return switch (config.browser()) {
            case "chrome", "remote-chrome" -> createChromeDriver(config);
            case "firefox" -> unsupportedBrowser("firefox", "P3.3 implements Firefox sessions");
            case "edge", "microsoftedge" -> unsupportedBrowser("microsoftedge", "P3.3 implements Edge sessions");
            case "opera" -> unsupportedBrowser("opera", "P3.3 removes Opera from the supported matrix");
            default -> throw new IllegalStateException("Unexpected browser: " + config.browser());
        };
    }

    private @NotNull RemoteWebDriver createChromeDriver(@NotNull TestRunConfig config) {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("prefs", chromePreferences());
        chromeOptions.addArguments(
                "download.prompt_for_download",
                "false",
                "safebrowsing.enabled",
                "false",
                "--remote-allow-origins=*",
                "--ignore-certificate-errors",
                "--disable-gpu",
                "--disable-web-security",
                "--allow-running-insecure-content",
                "--ignore_ssl",
                "--start-maximized",
                "--disable-infobars",
                "--test-type",
                "--disable-extensions",
                "--disable-dev-shm-usage",
                "--no-sandbox");

        if (config.headless() && !config.useSeleniumGrid()) {
            chromeOptions.addArguments("--headless", "--window-size=1920,1200", "--no-sandbox");
        }

        if (!config.browserVersion().isBlank() && !config.browserVersion().equals("''")) {
            chromeOptions.setBrowserVersion(config.browserVersion());
        }

        if (!config.usesRemoteDriver()) {
            return new ChromeDriver(chromeOptions);
        }

        chromeOptions.setCapability("se:recordVideo", true);
        chromeOptions.setCapability("se:timeZone", "US/Pacific");
        chromeOptions.setCapability("se:screenResolution", "1920x1080");

        RemoteWebDriver remoteDriver = new RemoteWebDriver(remoteUrl(config), chromeOptions);
        remoteDriver.setFileDetector(new LocalFileDetector());
        return remoteDriver;
    }

    private static @NotNull Map<String, Object> chromePreferences() {
        Map<String, Object> chromePreferences = new HashMap<>();
        chromePreferences.put("download.default_directory", DOWNLOADS_FOLDER);
        chromePreferences.put("download.prompt_for_download", false);
        chromePreferences.put("profile.default_content_settings.popups", 0);
        return chromePreferences;
    }

    private static @NotNull URL remoteUrl(@NotNull TestRunConfig config) {
        try {
            return new URL(config.seleniumGridUrl());
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid Selenium Grid URL: " + config.seleniumGridUrl(), e);
        }
    }

    private static @NotNull RemoteWebDriver unsupportedBrowser(@NotNull String browser, @NotNull String reason) {
        throw new UnsupportedOperationException("Browser is not supported yet: " + browser + ". " + reason + ".");
    }
}
