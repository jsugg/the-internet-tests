package theinternetwebsite.ui.pageobjects;

import java.time.Duration;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import theinternetwebsite.ui.UITest;

abstract class BasePage {
    private final UITest caller;
    private final String pageUrl;

    protected BasePage(@NotNull UITest caller, @NotNull String path) {
        this.caller = caller;
        this.pageUrl = caller.urlFor(path);
        PageFactory.initElements(caller.getDriver(), this);
    }

    public void open() {
        driver().get(pageUrl);
        waitUntilOpen();
    }

    public boolean isPageOpen() {
        return caller.isPageOpen(pageUrl, pageTitle());
    }

    protected abstract @NotNull WebElement pageTitle();

    protected final @NotNull UITest caller() {
        return caller;
    }

    protected final @NotNull RemoteWebDriver driver() {
        return caller.getDriver();
    }

    protected final @NotNull String pageUrl() {
        return pageUrl;
    }

    protected final @NotNull WebDriverWait waitFor(@NotNull Duration timeout) {
        return caller.waits().waitFor(timeout);
    }

    protected final @NotNull WebDriverWait defaultWait() {
        return caller.waits().defaultWait();
    }

    protected void waitUntilOpen() {
        caller.pageFactoryInitWait(pageTitle());
    }
}
