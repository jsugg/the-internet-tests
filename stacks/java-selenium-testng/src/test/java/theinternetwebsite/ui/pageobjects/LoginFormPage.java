package theinternetwebsite.ui.pageobjects;

import java.time.Duration;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import theinternetwebsite.ui.UITest;

public class LoginFormPage extends BasePage {
    @FindBy(how = How.XPATH, using = "//h2[normalize-space()='Login Page']")
    private WebElement pageTitle;
    @FindBy(how = How.XPATH, using = "//input[@id = 'username']")
    private WebElement username;
    @FindBy(how = How.XPATH, using = "//input[@id='password']")
    private WebElement password;
    @FindBy(how = How.XPATH, using = "//button[@type='submit']")
    private WebElement loginButton;
    @FindBy(how = How.XPATH, using = "//*[@class='flash error']")
    private WebElement errorMessage;

    private final String expectedLoggedInPageUrl;

    public LoginFormPage(@NotNull UITest caller) {
        super(caller, "/login");
        this.expectedLoggedInPageUrl = caller.urlFor("/secure");
    }

    @Override
    protected @NotNull WebElement pageTitle() {
        return pageTitle;
    }

    public String getExpectedLoggedInPageUrl() {
        return expectedLoggedInPageUrl;
    }

    public void setUsername(String username) {
        waitFor(Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOf(this.username));
        this.username.clear();
        this.username.sendKeys(username);
    }

    public void setPassword(String password) {
        waitFor(Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOf(this.password));
        this.password.clear();
        this.password.sendKeys(password);
    }

    public void clickLoginButton() {
        waitFor(Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOf(this.loginButton));
        loginButton.click();
    }

    public String getErrorMessage() {
        return waitFor(Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOf(this.errorMessage)).getText();
    }
}
