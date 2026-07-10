package theinternetwebsite.ui;

import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import theinternetwebsite.ui.driver.DriverFactory;
import theinternetwebsite.ui.driver.TestRunConfig;
import theinternetwebsite.ui.support.BrowserActions;
import theinternetwebsite.ui.support.BrowserWaits;
import theinternetwebsite.ui.support.TextContent;

public class UITest {
    public static final String DEFAULT_BROWSER = "chrome";
    public static final String DEFAULT_BROWSER_HEADLESS = "false";
    public static final String downloadsFolder = DriverFactory.DOWNLOADS_FOLDER;

    private final DriverFactory driverFactory = new DriverFactory();
    private final ThreadLocal<TestRunConfig> config = new ThreadLocal<>();
    private final ThreadLocal<RemoteWebDriver> driver = new ThreadLocal<>();
    private final ThreadLocal<BrowserWaits> waits = new ThreadLocal<>();

    public UITest() { }

    @Parameters({"browser", "browserVersion", "headlessBrowser", "baseUrl", "baseUrlSG", "seleniumGridUrl", "useSeleniumGrid"})
    @BeforeTest
    public void setUp(
            @NotNull String browser,
            @NotNull String browserVersion,
            @NotNull String headless,
            @NotNull String baseUrl,
            @NotNull String baseUrlSG,
            @NotNull String remoteUrl,
            @NotNull String useSeleniumGrid) {
        TestRunConfig runConfig = TestRunConfig.from(browser, browserVersion, headless, baseUrl, baseUrlSG, remoteUrl, useSeleniumGrid);
        RemoteWebDriver runDriver = driverFactory.create(runConfig);
        config.set(runConfig);
        driver.set(runDriver);
        waits.set(new BrowserWaits(runDriver));
    }

    @AfterTest
    public void tearDown() {
        RemoteWebDriver currentDriver = driver.get();
        try {
            if (currentDriver != null) {
                currentDriver.quit();
            }
        } finally {
            driver.remove();
            waits.remove();
            config.remove();
        }
    }

    public void dragAndDropJS(@NotNull WebElement source, @NotNull WebElement destination) {
        BrowserActions.dragAndDropWithJavaScript(getDriver(), source, destination);
    }

    public static void reloadPage(@NotNull RemoteWebDriver driver) {
        BrowserActions.reloadPage(driver);
    }

    public static @NotNull String cleanTextContent(@NotNull String text) {
        return TextContent.clean(text);
    }

    public @NotNull RemoteWebDriver getDriver() {
        RemoteWebDriver currentDriver = driver.get();
        if (currentDriver == null) {
            throw new IllegalStateException("WebDriver is not initialized. Check the TestNG setup parameters.");
        }
        return currentDriver;
    }

    public @NotNull BrowserWaits waits() {
        BrowserWaits currentWaits = waits.get();
        if (currentWaits == null) {
            throw new IllegalStateException("Browser waits are not initialized. Check the WebDriver lifecycle.");
        }
        return currentWaits;
    }

    public @NotNull String getBaseUrl() {
        return activeConfig().activeBaseUrl();
    }

    public @NotNull String urlFor(@NotNull String path) {
        if (path.startsWith("http://") || path.startsWith("https://")) {
            return path;
        }
        String normalizedBaseUrl = getBaseUrl().replaceAll("/+$", "");
        String normalizedPath = path.startsWith("/") ? path : "/" + path;
        return normalizedBaseUrl + normalizedPath;
    }

    public final boolean isPageOpen(@NotNull String pageUrl, @NotNull WebElement pageTitle) {
        return waits().isPageOpen(pageUrl, pageTitle);
    }

    public final boolean isPageOpen(@NotNull String pageUrl) {
        return waits().isPageOpen(pageUrl);
    }

    public final @NotNull WebElement getPageFooter() {
        return waits().pageFooter();
    }

    public final void pageFactoryInitWait(@NotNull WebElement pageTitle) {
        waits().waitForPageFactoryElements(pageTitle);
    }

    public @NotNull String getCurrentBrowser() {
        return activeConfig().browser();
    }

    private @NotNull TestRunConfig activeConfig() {
        TestRunConfig currentConfig = config.get();
        if (currentConfig == null) {
            throw new IllegalStateException("Test run config is not initialized. Check the TestNG setup parameters.");
        }
        return currentConfig;
    }
}
