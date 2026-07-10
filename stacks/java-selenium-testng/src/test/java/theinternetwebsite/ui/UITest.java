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
import theinternetwebsite.ui.support.HttpFileDownloader;
import theinternetwebsite.ui.support.TextContent;

public class UITest {
    public static final String DEFAULT_BROWSER = "chrome";
    public static final String DEFAULT_BROWSER_HEADLESS = "false";
    public static final String downloadsFolder = DriverFactory.DOWNLOADS_FOLDER;

    private final DriverFactory driverFactory = new DriverFactory();
    private TestRunConfig config;
    private RemoteWebDriver driver;
    private BrowserWaits waits;

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
        config = TestRunConfig.from(browser, browserVersion, headless, baseUrl, baseUrlSG, remoteUrl, useSeleniumGrid);
        driver = driverFactory.create(config);
        waits = new BrowserWaits(driver);
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            driver = null;
            waits = null;
            config = null;
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
        if (driver == null) {
            throw new IllegalStateException("WebDriver is not initialized. Check the TestNG setup parameters.");
        }
        return driver;
    }

    public @NotNull BrowserWaits waits() {
        if (waits == null) {
            throw new IllegalStateException("Browser waits are not initialized. Check the WebDriver lifecycle.");
        }
        return waits;
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

    public void downloadFileHeadless(@NotNull String fileUrl, @NotNull String localFilePath) {
        HttpFileDownloader.download(fileUrl, localFilePath);
    }

    private @NotNull TestRunConfig activeConfig() {
        if (config == null) {
            throw new IllegalStateException("Test run config is not initialized. Check the TestNG setup parameters.");
        }
        return config;
    }
}
