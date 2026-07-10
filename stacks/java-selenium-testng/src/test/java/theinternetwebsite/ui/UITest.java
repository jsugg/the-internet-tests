package theinternetwebsite.ui;

import java.net.URL;
import java.util.Map;
import java.util.HashMap;
import java.time.Duration;
import java.nio.file.Paths;
import org.openqa.selenium.By;
import org.testng.annotations.*;
import org.openqa.selenium.WebElement;
import java.net.MalformedURLException;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

public class UITest {
    public static final String DEFAULT_BROWSER = "chrome";
    public static final String DEFAULT_BROWSER_HEADLESS = "false";
    public static final String downloadsFolder = Paths.get("target/").toAbsolutePath().toString();
    private String currentBrowser;
    private RemoteWebDriver driver;
    private String baseUrl;
    private static final String pageFooterXpath = "//*[@id='page-footer']";

    public UITest() { }
    @Parameters({"browser", "browserVersion", "headlessBrowser", "baseUrl", "baseUrlSG", "seleniumGridUrl", "useSeleniumGrid"})
    @BeforeTest
    public void setUp( String browser, String browserVersion, String headless, String baseUrl, String baseUrlSG, String remoteUrl, @NotNull String useSeleniumGrid) {
        if (useSeleniumGrid.equals("true")) { this.setBaseUrl(baseUrlSG); } else { this.setBaseUrl(baseUrl); }

        browser = browser.toLowerCase();
        this.setCurrentBrowser(browser);

        switch (browser) {
            case "chrome", "remote-chrome" ->
                    this.setDriver(requestChromeDriver(browser, browserVersion, headless, remoteUrl, useSeleniumGrid));
            case "firefox" ->
                // Firefox To be implemented
                    this.setDriver(requestFirefoxDriver(browserVersion, headless, remoteUrl, useSeleniumGrid));
            case "microsoftedge" ->
                // Edge To be implemented
                    this.setDriver(requestMicrosoftEdgeDriver(browserVersion, headless, remoteUrl, useSeleniumGrid));
            case "opera" ->
                // Opera To be implemented
                    this.setDriver(requestOperaDriver(browserVersion, headless, remoteUrl, useSeleniumGrid));
            default -> throw new IllegalStateException("Unexpected value: " + browser);
        }
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private RemoteWebDriver requestChromeDriver(String browser, String browserVersion, String headless, String remoteUrl, @NotNull String useSeleniumGrid) {
        ChromeOptions chromeOptions = new ChromeOptions();
        Map<String, Object> chromeExpOptions = new HashMap<>();

        // Local session - Set experimental options
        //if (!useSeleniumGrid.equals("true")) {
        chromeExpOptions.put("download.default_directory", downloadsFolder);
        //}
        chromeExpOptions.put("download.prompt_for_download", false);
        chromeExpOptions.put("profile.default_content_settings.popups", 0); //

        chromeOptions.setExperimentalOption("prefs", chromeExpOptions);

        // Common + hacky options
        chromeOptions.addArguments("download.prompt_for_download", "false");
        chromeOptions.addArguments("safebrowsing.enabled", "false");
        chromeOptions.addArguments("--remote-allow-origins=*");
        chromeOptions.addArguments("--ignore-certificate-errors");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--disable-web-security");
        chromeOptions.addArguments("--allow-running-insecure-content");
        chromeOptions.addArguments("--ignore_ssl");
        chromeOptions.addArguments("--start-maximized"); //
        chromeOptions.addArguments("--disable-infobars"); //
        chromeOptions.addArguments("--test-type"); //
        chromeOptions.addArguments("--disable-extensions"); //
        chromeOptions.addArguments("--disable-dev-shm-usage"); //
        chromeOptions.addArguments("--no-sandbox"); //
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        //capabilities.setCapability(CapabilityType.SUPPORTS_NETWORK_CONNECTION, true);
        capabilities.setCapability("se:recordVideo", true);
        capabilities.setCapability("se:timeZone", "US/Pacific");
        capabilities.setCapability("se:screenResolution", "1920x1080");

        // Use headless only on local runs. When using selenium grid, download fails in Chrome because of a bug.
        if (headless.equals("true") && useSeleniumGrid.equals("false")) {
            chromeOptions.addArguments("--headless", "--window-size=1920,1200", "--no-sandbox"); }

        // Remote driver session
        if (useSeleniumGrid.equals("true") || browser.equals("remote-chrome")) {
            try {
                this.driver = new RemoteWebDriver(new URL(remoteUrl), capabilities);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            this.driver.setFileDetector(new LocalFileDetector());
            return driver;
        } else {
            return new ChromeDriver(chromeOptions);
        }
    }

    private RemoteWebDriver requestFirefoxDriver(String browserVersion, String headless, String remoteUrl, String useSeleniumGrid) {
        return null;
    }

    private RemoteWebDriver requestMicrosoftEdgeDriver(String browserVersion, String headless, String remoteUrl, String useSeleniumGrid) {
        return null;
    }

    private RemoteWebDriver requestOperaDriver(String browserVersion, String headless, String remoteUrl, String useSeleniumGrid) {
        return null;
    }

    public void dragAndDropJS(WebElement source, WebElement destination)
    {
        JavascriptExecutor js = this.getDriver();
        js.executeScript("""
                function createEvent(typeOfEvent) {
                var event = document.createEvent('CustomEvent');
                event.initCustomEvent(typeOfEvent,true, true, null);
                event.dataTransfer = {
                data: {},
                setData: function (key, value) {
                this.data[key] = value;
                },
                getData: function (key) {
                return this.data[key];
                }
                };
                return event;
                }

                function dispatchEvent(element, event,transferData) {
                if (transferData !== undefined) {
                event.dataTransfer = transferData;
                }
                if (element.dispatchEvent) {
                element.dispatchEvent(event);
                } else if (element.fireEvent) {
                element.fireEvent("on" + event.type, event);
                }
                }

                function simulateHTML5DragAndDrop(element, destination) {
                var dragStartEvent =createEvent('dragstart');
                dispatchEvent(element, dragStartEvent);
                var dropEvent = createEvent('drop');
                dispatchEvent(destination, dropEvent,dragStartEvent.dataTransfer);
                var dragEndEvent = createEvent('dragend');
                dispatchEvent(element, dragEndEvent,dropEvent.dataTransfer);
                }

                var source = arguments[0];
                var destination = arguments[1];
                simulateHTML5DragAndDrop(source,destination);""",source, destination);
    }
    public static void reloadPage(@NotNull RemoteWebDriver driver) { driver.navigate().refresh(); }

    public static @NotNull String cleanTextContent(String text)
    {
        // strips off all non-ASCII characters
        text = text.replaceAll("[^\\x00-\\x7F]", "");
        // erases all the ASCII control characters
        text = text.replaceAll("[\\p{Cntrl}&&[^\\r\\n\\t]]", "");
        // removes non-printable characters from Unicode
        text = text.replaceAll("\\p{C}", "");
        return text.trim();
    }

    public RemoteWebDriver getDriver() {
        return this.driver;
    }

    public void setDriver(RemoteWebDriver driver) {
        this.driver = driver;
    }

    public String getBaseUrl() {
        return this.baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public final @NotNull Boolean isPageOpen(String pageUrl, WebElement pageTitle) {
        WebDriverWait genericWait = new WebDriverWait(this.getDriver(), Duration.ofSeconds(10));
        genericWait.until(ExpectedConditions.visibilityOf(pageTitle));
        return this.getDriver().getCurrentUrl().equals(pageUrl) && pageTitle.isDisplayed();
    }
    public final @NotNull Boolean isPageOpen(String pageUrl) { return this.getDriver().getCurrentUrl().equals(pageUrl); }

    public final WebElement getPageFooter() { return this.getDriver().findElement(By.xpath(pageFooterXpath)); }

    public final void pageFactoryInitWait(WebElement pageTitle) {
        WebDriverWait pageFactoryInitWait = new WebDriverWait(this.getDriver(), Duration.ofSeconds(10), Duration.ofSeconds(5));
        pageFactoryInitWait.until(ExpectedConditions.and(visibilityOf(pageTitle), visibilityOf(this.getPageFooter())));
    }

    public String getCurrentBrowser() {
        return currentBrowser;
    }

    public void setCurrentBrowser(String currentBrowser) {
        this.currentBrowser = currentBrowser;
    }

    public void downloadFileHeadless(String fileUrl, String localFilePath) {
        try {
            URL url = new URL(fileUrl);
            URLConnection connection = url.openConnection();
            InputStream inputStream = new BufferedInputStream(connection.getInputStream());
            FileOutputStream outputStream = new FileOutputStream(localFilePath);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer, 0, 1024)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
