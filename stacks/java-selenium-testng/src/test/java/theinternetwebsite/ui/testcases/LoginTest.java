package theinternetwebsite.ui.testcases;

import org.testng.Assert;
import org.testng.annotations.Test;
import theinternetwebsite.ui.UITest;
import org.testng.annotations.Optional;
import org.jetbrains.annotations.NotNull;
import org.testng.annotations.Parameters;
import theinternetwebsite.ui.pageobjects.LoginFormPage;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginTest extends UITest {

    //private final String username;
    private static final String DEFAULT_USERNAME  = "tomsmith";
    //private final String password;
    private static final String DEFAULT_PASSWORD = "SuperSecretPassword!";
    //private final String invalid;
    private static final String DEFAULT_INVALID_USERNAME = "invalidUsername";
    private static final String DEFAULT_INVALID_PASSWORD = "invalidPassword";

    public LoginTest() {}

    @Parameters({"username", "password"})
    @Test(description="Login form - Using valid Credentials", groups={ "loginForm" }, testName="UI-LOGIN-001")
    public void successfulLogin(@Optional(DEFAULT_USERNAME) String username, @Optional(DEFAULT_PASSWORD) String password) {
        LoginFormPage loginFormPage = new LoginFormPage(this);
        loginFormPage.open();

        // Validate page loaded
        Assert.assertTrue(loginFormPage.isPageOpen(), "Page not open");

        // Validate login succeeded
        this.login(loginFormPage, username, password);
        new WebDriverWait(this.getDriver(), Duration.ofSeconds(10))
                .until(ExpectedConditions.urlToBe(loginFormPage.getExpectedLoggedInPageUrl()));
        Assert.assertEquals(this.getDriver().getCurrentUrl(), loginFormPage.getExpectedLoggedInPageUrl());
    }

    @Parameters({"username", "password"})
    @Test(description="Login form - Using invalid Username", groups={ "loginForm" }, testName="UI-LOGIN-002")
    public void invalidUsername(@Optional(DEFAULT_INVALID_USERNAME) String username, @Optional(DEFAULT_PASSWORD) String password) {
        String expectedUserErrorMessage = "Your username is invalid!";
        LoginFormPage loginFormPage = this.login(username, password);

        new WebDriverWait(this.getDriver(), Duration.ofSeconds(10))
                .until(ExpectedConditions.urlToBe(this.getBaseUrl() + "/login"));

        // Validate login failed due to invalid username
        Assert.assertNotEquals(this.getDriver().getCurrentUrl(), loginFormPage.getExpectedLoggedInPageUrl());
        Assert.assertTrue(loginFormPage.getErrorMessage().contains(expectedUserErrorMessage), "expectedErrorMessage mismatch");
    }

    @Parameters({"username", "password"})
    @Test(description="Login form - Using invalid Password", groups={ "loginForm" }, testName="UI-LOGIN-003")
    public void invalidPassword(@Optional(DEFAULT_USERNAME) String username, @Optional(DEFAULT_INVALID_PASSWORD) String password) {
        String expectedPasswordErrorMessage = "Your password is invalid!";
        LoginFormPage loginFormPage = this.login(username, password);

        new WebDriverWait(this.getDriver(), Duration.ofSeconds(10))
                .until(ExpectedConditions.urlToBe(this.getBaseUrl() + "/login"));

        // Validate login failed due to invalid password
        Assert.assertNotEquals(this.getDriver().getCurrentUrl(), loginFormPage.getExpectedLoggedInPageUrl());
        Assert.assertTrue(loginFormPage.getErrorMessage().contains(expectedPasswordErrorMessage), "expectedErrorMessage mismatch");
    }

    private @NotNull LoginFormPage login(String username, String password) {
        LoginFormPage loginFormPage = new LoginFormPage(this);
        loginFormPage.open();
        return this.login(loginFormPage, username, password);
    }

    private @NotNull LoginFormPage login(LoginFormPage loginFormPage, String username, String password) {
        loginFormPage.setUsername(username);
        loginFormPage.setPassword(password);
        loginFormPage.clickLoginButton();
        return loginFormPage;
    }
}
