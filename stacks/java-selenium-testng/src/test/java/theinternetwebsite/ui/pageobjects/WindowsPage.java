package theinternetwebsite.ui.pageobjects;

import java.time.Duration;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import theinternetwebsite.ui.UITest;

import static org.openqa.selenium.support.ui.ExpectedConditions.numberOfWindowsToBe;
import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

public class WindowsPage extends BasePage {
    @FindBy(how = How.XPATH, using = "//h3[normalize-space()='Opening a new window']")
    private WebElement pageTitle;
    @FindBy(how = How.XPATH, using = "//a[normalize-space()='Click Here']")
    private WebElement link;
    @FindBy(how = How.XPATH, using = "//h3[normalize-space()='New Window']")
    private WebElement newTab;

    public WindowsPage(@NotNull UITest caller) {
        super(caller, "/windows");
    }

    @Override
    protected @NotNull WebElement pageTitle() {
        return pageTitle;
    }

    public void clickLink() {
        this.link.click();
    }

    public boolean validateNewTab() {
        String originalWindow = driver().getWindowHandle();
        String expectedMessage = "New Window";

        this.clickLink();
        waitFor(Duration.ofSeconds(30)).until(numberOfWindowsToBe(2));

        for (String windowHandle : driver().getWindowHandles()) {
            if (!originalWindow.contentEquals(windowHandle)) {
                driver().switchTo().window(windowHandle);
                waitFor(Duration.ofSeconds(30)).until(titleIs("New Window"));
                return newTab.getText().equals(expectedMessage);
            }
        }
        throw new IllegalStateException("New window handle was not found");
    }
}
