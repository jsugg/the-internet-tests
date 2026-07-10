package theinternetwebsite.ui.pageobjects;

import java.time.Duration;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import theinternetwebsite.ui.UITest;

public class JavascriptErrorPage extends BasePage {
    @FindBy(how = How.XPATH, using = "//p[contains(text(),'This page has a JavaScript error in the onload eve')]")
    private WebElement pageErrorMessage;

    public JavascriptErrorPage(@NotNull UITest caller) {
        super(caller, "/javascript_error");
    }

    @Override
    protected @NotNull WebElement pageTitle() {
        return pageErrorMessage;
    }

    @Override
    protected void waitUntilOpen() {
        waitFor(Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOf(pageErrorMessage));
    }

    public boolean validateErrorMessage() {
        String expectedMessage = "Uncaught TypeError: Cannot read properties of undefined (reading 'xyz')";
        for (LogEntry log : driver().manage().logs().get(LogType.BROWSER)) {
            if (log.getMessage().contains(expectedMessage)) {
                return true;
            }
        }
        return false;
    }
}
