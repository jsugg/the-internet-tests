package theinternetwebsite.ui.pageobjects;

import java.time.Duration;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import theinternetwebsite.ui.UITest;

public class DynamicLoadingPage extends BasePage {
    @FindBy(how = How.XPATH, using = "//h3[normalize-space()='Dynamically Loaded Page Elements']")
    private WebElement pageTitle;
    @FindBy(how = How.XPATH, using = "//button[text()= 'Start']")
    private WebElement startButton;
    @FindBy(how = How.XPATH, using = "//*[@id='finish']")
    private WebElement successMessage;

    public DynamicLoadingPage(@NotNull UITest caller) {
        super(caller, "/dynamic_loading/2");
    }

    @Override
    protected @NotNull WebElement pageTitle() {
        return pageTitle;
    }

    public void startMessageRequest() {
        startButton.click();
        waitFor(Duration.ofSeconds(30)).until(ExpectedConditions.visibilityOf(successMessage));
    }

    public String getSuccessMessage() {
        return successMessage.getText();
    }
}
