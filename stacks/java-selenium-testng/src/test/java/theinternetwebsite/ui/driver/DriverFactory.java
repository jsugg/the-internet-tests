package theinternetwebsite.ui.driver;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

public final class DriverFactory {
    public static final String DOWNLOADS_FOLDER = Paths.get("target/").toAbsolutePath().toString();

    public @NotNull RemoteWebDriver create(@NotNull TestRunConfig config) {
        return switch (config.browser()) {
            case "chrome", "remote-chrome" -> createChromeDriver(config);
            case "firefox" -> createFirefoxDriver(config);
            case "edge", "microsoftedge" -> createEdgeDriver(config);
            default -> throw new IllegalStateException("Unexpected browser: " + config.browser());
        };
    }

    private @NotNull RemoteWebDriver createChromeDriver(@NotNull TestRunConfig config) {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("prefs", chromePreferences());
        chromeOptions.addArguments("--window-size=1920,1200");
        if (config.headless() && !config.useSeleniumGrid()) {
            chromeOptions.addArguments("--headless=new");
        }
        applyBrowserVersion(chromeOptions, config);
        applyGridCapabilities(chromeOptions, config);

        if (config.usesRemoteDriver()) {
            return remoteDriver(config, chromeOptions);
        }
        return new ChromeDriver(chromeOptions);
    }

    private @NotNull RemoteWebDriver createFirefoxDriver(@NotNull TestRunConfig config) {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.addArguments("--width=1920", "--height=1200");
        if (config.headless() && !config.useSeleniumGrid()) {
            firefoxOptions.addArguments("-headless");
        }
        applyBrowserVersion(firefoxOptions, config);
        applyGridCapabilities(firefoxOptions, config);

        if (config.usesRemoteDriver()) {
            return remoteDriver(config, firefoxOptions);
        }
        return new FirefoxDriver(firefoxOptions);
    }

    private @NotNull RemoteWebDriver createEdgeDriver(@NotNull TestRunConfig config) {
        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.setExperimentalOption("prefs", chromePreferences());
        edgeOptions.addArguments("--window-size=1920,1200");
        if (config.headless() && !config.useSeleniumGrid()) {
            edgeOptions.addArguments("--headless=new");
        }
        applyBrowserVersion(edgeOptions, config);
        applyGridCapabilities(edgeOptions, config);

        if (config.usesRemoteDriver()) {
            return remoteDriver(config, edgeOptions);
        }
        return new EdgeDriver(edgeOptions);
    }

    private static void applyBrowserVersion(@NotNull ChromeOptions options, @NotNull TestRunConfig config) {
        if (hasBrowserVersion(config)) {
            options.setBrowserVersion(config.browserVersion());
        }
    }

    private static void applyBrowserVersion(@NotNull FirefoxOptions options, @NotNull TestRunConfig config) {
        if (hasBrowserVersion(config)) {
            options.setBrowserVersion(config.browserVersion());
        }
    }

    private static void applyBrowserVersion(@NotNull EdgeOptions options, @NotNull TestRunConfig config) {
        if (hasBrowserVersion(config)) {
            options.setBrowserVersion(config.browserVersion());
        }
    }

    private static boolean hasBrowserVersion(@NotNull TestRunConfig config) {
        return !config.browserVersion().isBlank() && !config.browserVersion().equals("''");
    }

    private static void applyGridCapabilities(@NotNull ChromeOptions options, @NotNull TestRunConfig config) {
        if (config.usesRemoteDriver()) {
            setGridCapabilities(options);
        }
    }

    private static void applyGridCapabilities(@NotNull FirefoxOptions options, @NotNull TestRunConfig config) {
        if (config.usesRemoteDriver()) {
            setGridCapabilities(options);
        }
    }

    private static void applyGridCapabilities(@NotNull EdgeOptions options, @NotNull TestRunConfig config) {
        if (config.usesRemoteDriver()) {
            setGridCapabilities(options);
        }
    }

    private static void setGridCapabilities(@NotNull org.openqa.selenium.MutableCapabilities options) {
        options.setCapability("se:recordVideo", true);
        options.setCapability("se:timeZone", "US/Pacific");
        options.setCapability("se:screenResolution", "1920x1080");
    }

    private static @NotNull RemoteWebDriver remoteDriver(
            @NotNull TestRunConfig config, @NotNull Capabilities capabilities) {
        RemoteWebDriver remoteDriver = new RemoteWebDriver(remoteUrl(config), capabilities);
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
}
