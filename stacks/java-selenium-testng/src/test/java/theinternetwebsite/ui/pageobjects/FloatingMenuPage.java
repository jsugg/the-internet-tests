package theinternetwebsite.ui.pageobjects;

import java.time.Duration;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import theinternetwebsite.ui.UITest;

public class FloatingMenuPage extends BasePage {
    @FindBy(how = How.XPATH, using = "//h3[normalize-space()='Floating Menu']")
    private WebElement pageTitle;
    @FindBy(how = How.XPATH, using = "//*[@id='menu']")
    private WebElement menu;
    @FindBy(how = How.XPATH, using = "//body")
    private WebElement pageBody;
    @FindBy(how = How.XPATH, using = "//*[@id='page-footer']/div/div/a")
    private WebElement elementalSeleniumLink;

    public FloatingMenuPage(@NotNull UITest caller) {
        super(caller, "/floating_menu");
    }

    @Override
    protected @NotNull WebElement pageTitle() {
        return pageTitle;
    }

    public String getMenuPosition() {
        return menu.getAttribute("style");
    }

    public void scrollToBottom() {
        waitFor(Duration.ofSeconds(30)).until(ExpectedConditions.visibilityOf(elementalSeleniumLink));
        for (int i = 3; i > 0; i--) {
            pageBody.sendKeys(Keys.END, Keys.CONTROL);
        }
        pageBody.sendKeys(Keys.END, Keys.CONTROL);
        waitFor(Duration.ofSeconds(30)).until(ExpectedConditions.visibilityOf(elementalSeleniumLink));
    }

    public boolean validateMenuVisibility() {
        waitFor(Duration.ofSeconds(30)).until(ExpectedConditions.visibilityOf(menu));
        return menu.isDisplayed();
    }
}
