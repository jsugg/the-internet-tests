package theinternetwebsite.ui.pageobjects;

import org.openqa.selenium.Keys;
import theinternetwebsite.ui.UITest;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

public class ContextMenuPage {

    @FindBy(how = How.XPATH, using = "//h3[normalize-space()='Context Menu']")
    private WebElement pageTitle;

    @FindBy(id = "hot-spot")
    private WebElement box;

    private final UITest caller;
    private final String pageUrl;

    public ContextMenuPage(@NotNull UITest caller) {
        this.caller = caller;
        this.pageUrl = this.caller.getBaseUrl() + "/context_menu";
        this.caller.getDriver().get(this.pageUrl);
        PageFactory.initElements(this.caller.getDriver(), this);
        this.caller.pageFactoryInitWait(pageTitle);
    }

    public boolean isPageOpen() {
        return this.caller.isPageOpen(this.pageUrl, this.pageTitle);
    }

    public void rightClickBox() {
        Actions builder = new Actions(caller.getDriver());
        builder.contextClick(this.box).perform();
    }

    public String getAlertPopupText() {
        Alert alert = caller.getDriver().switchTo().alert();
        String alertPopupText = alert.getText();
        alert.accept();
        return alertPopupText;
    }
}
