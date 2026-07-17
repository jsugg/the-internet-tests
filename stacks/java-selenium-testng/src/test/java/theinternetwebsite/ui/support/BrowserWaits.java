package theinternetwebsite.ui.support;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import java.time.Duration;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public final class BrowserWaits {
    private static final By PAGE_FOOTER = By.xpath("//*[@id='page-footer']");
    private final WebDriver driver;

    public BrowserWaits(@NotNull WebDriver driver) {
        this.driver = driver;
    }

    public @NotNull WebDriverWait defaultWait() {
        return new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public @NotNull WebDriverWait waitFor(@NotNull Duration timeout) {
        return new WebDriverWait(driver, timeout);
    }

    public boolean isPageOpen(@NotNull String pageUrl, @NotNull WebElement pageTitle) {
        defaultWait().until(ExpectedConditions.visibilityOf(pageTitle));
        return driver.getCurrentUrl().equals(pageUrl) && pageTitle.isDisplayed();
    }

    public boolean isPageOpen(@NotNull String pageUrl) {
        return driver.getCurrentUrl().equals(pageUrl);
    }

    public @NotNull WebElement pageFooter() {
        return driver.findElement(PAGE_FOOTER);
    }

    public void waitForPageFactoryElements(@NotNull WebElement pageTitle) {
        waitFor(Duration.ofSeconds(10)).until(ExpectedConditions.and(visibilityOf(pageTitle), visibilityOfElementLocated(PAGE_FOOTER)));
    }
}
