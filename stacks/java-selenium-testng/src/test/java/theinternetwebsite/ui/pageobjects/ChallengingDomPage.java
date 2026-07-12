package theinternetwebsite.ui.pageobjects;

import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import theinternetwebsite.ui.UITest;

public class ChallengingDomPage extends BasePage {
    @FindBy(css = "h3")
    private WebElement pageTitle;

    public ChallengingDomPage(@NotNull UITest caller) {
        super(caller, "/challenging_dom");
    }

    @Override
    protected @NotNull WebElement pageTitle() {
        return pageTitle;
    }

    public int buttonCount() {
        return driver().findElements(By.cssSelector(".button")).size();
    }

    public boolean isCanvasVisible() {
        return driver().findElement(By.id("canvas")).isDisplayed();
    }

    public @NotNull List<String> tableHeaders() {
        return driver().findElements(By.cssSelector("table thead th")).stream()
                .map(WebElement::getText)
                .toList();
    }

    public int tableRowCount() {
        return driver().findElements(By.cssSelector("table tbody tr")).size();
    }

    public @NotNull String firstRowText() {
        return driver().findElement(By.cssSelector("table tbody tr:first-child")).getText();
    }

    public @NotNull String firstEditHref() {
        return firstActionHref("edit");
    }

    public @NotNull String firstDeleteHref() {
        return firstActionHref("delete");
    }

    private @NotNull String firstActionHref(@NotNull String actionName) {
        WebElement actionLink =
                driver().findElement(By.xpath("//table//tbody/tr[1]//a[normalize-space()='" + actionName + "']"));
        return Objects.requireNonNull(actionLink.getAttribute("href"), "Expected action link href");
    }
}
