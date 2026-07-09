package theinternetwebsite.ui.pageobjects;

import theinternetwebsite.ui.UITest;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class DynamicLoadingPage {

    @FindBy(how = How.XPATH, using = "//h3[normalize-space()='Dynamically Loaded Page Elements']")
    public WebElement pageTitle;
    @FindBy(how = How.XPATH, using = "//button[text()= 'Start']")
    public WebElement startButton;
    @FindBy(how = How.XPATH, using = "//*[@id='finish']")
    public WebElement successMessage;
    private final UITest caller;
    private final String pageUrl;

    public DynamicLoadingPage(UITest caller) {
        this.caller = caller;
        this.pageUrl = this.caller.getBaseUrl() + "/dynamic_loading/2";
        this.caller.getDriver().get(this.pageUrl);
        PageFactory.initElements(this.caller.getDriver(), this);
        this.caller.pageFactoryInitWait(pageTitle);
    }

    public Boolean isPageOpen() {
        try {
            return this.caller.isPageOpen(this.pageUrl, this.pageTitle);
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void startMessageRequest() {
        WebDriverWait wait = new WebDriverWait(caller.getDriver(), Duration.ofSeconds(30));
        startButton.click();
        wait.until(ExpectedConditions.visibilityOf(successMessage));
    }

    public String getSuccessMessage() {
        return successMessage.getText();
    }
}
