package theinternetwebsite.ui.pageobjects;

import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import theinternetwebsite.ui.UITest;

public class ContextMenuPage extends BasePage {
    @FindBy(how = How.XPATH, using = "//h3[normalize-space()='Context Menu']")
    private WebElement pageTitle;

    @FindBy(id = "hot-spot")
    private WebElement box;

    public ContextMenuPage(@NotNull UITest caller) {
        super(caller, "/context_menu");
    }

    @Override
    protected @NotNull WebElement pageTitle() {
        return pageTitle;
    }

    public void rightClickBox() {
        new Actions(driver()).contextClick(this.box).perform();
    }

    public String getAlertPopupText() {
        Alert alert = driver().switchTo().alert();
        String alertPopupText = alert.getText();
        alert.accept();
        return alertPopupText;
    }
}
