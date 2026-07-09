package theinternetwebsite.ui.pageobjects;

import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import theinternetwebsite.ui.UITest;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class FloatingMenuPage {

    @FindBy(how = How.XPATH, using = "//h3[normalize-space()='Floating Menu']")
    public WebElement pageTitle;
    @FindBy(how = How.XPATH, using = "//*[@id='menu']")
    public WebElement menu;
    @FindBy(how = How.XPATH, using = "//body")
    public WebElement pageBody;
    @FindBy(how = How.XPATH, using = "//*[@id='page-footer']/div/div/a")
    public WebElement elementalSeleniumLink;
    private final String pageUrl;
    private final UITest caller;
    private final WebDriverWait genericWait;

    public FloatingMenuPage(@NotNull UITest caller) {
        this.caller = caller;
        this.pageUrl = this.caller.getBaseUrl() + "/floating_menu";
        this.caller.getDriver().get(this.pageUrl);
        PageFactory.initElements(this.caller.getDriver(), this);
        this.caller.pageFactoryInitWait(pageTitle);
        this.genericWait = new WebDriverWait(caller.getDriver(), Duration.ofSeconds(30));
    }

    public Boolean isPageOpen() { return this.caller.isPageOpen(this.pageUrl, this.pageTitle); }

    public String getMenuPosition() {
        return menu.getAttribute("style"); }

    public void scrollToBottom() {
        this.genericWait.until(ExpectedConditions.visibilityOf(elementalSeleniumLink));
        for(int i=3; i>0; i--) {
            pageBody.sendKeys(Keys.END, Keys.CONTROL);
            try {
                this.genericWait.until(ExpectedConditions.visibilityOf(elementalSeleniumLink));
            } catch (TimeoutException ignored) { }
        }
        // Try one last time
        pageBody.sendKeys(Keys.END, Keys.CONTROL);
        this.genericWait.until(ExpectedConditions.visibilityOf(elementalSeleniumLink));
    }

    public Boolean validateMenuVisibility() {
        WebDriverWait wait = new WebDriverWait(caller.getDriver(), Duration.ofSeconds(30));
        wait.until(ExpectedConditions.visibilityOf(menu));
        return menu.isDisplayed(); }
}