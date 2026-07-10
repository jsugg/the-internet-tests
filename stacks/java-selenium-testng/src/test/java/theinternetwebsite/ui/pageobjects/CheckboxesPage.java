package theinternetwebsite.ui.pageobjects;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import theinternetwebsite.ui.UITest;

public class CheckboxesPage extends BasePage {
    private static final By CHECKBOXES = By.cssSelector("input[type='checkbox']");

    @FindBy(css = "h3")
    private WebElement pageTitle;

    private List<Boolean> currentValues = new ArrayList<>();
    private List<WebElement> checkboxes = new ArrayList<>();

    public CheckboxesPage(@NotNull UITest caller) {
        super(caller, "/checkboxes");
    }

    @Override
    protected @NotNull WebElement pageTitle() {
        return pageTitle;
    }

    public List<Boolean> getAllCheckboxCurrentValues() {
        this.checkboxes = driver().findElements(CHECKBOXES);
        this.currentValues = new ArrayList<>();
        for (WebElement checkboxElement : this.checkboxes) {
            currentValues.add(checkboxElement.isSelected());
        }
        return this.currentValues;
    }

    public boolean toggleCheckbox(int index) {
        WebElement checkboxElement = this.checkboxes.get(index);
        boolean currentValue = this.currentValues.get(index);
        checkboxElement.click();
        boolean newValue = !currentValue;
        this.currentValues.set(index, newValue);
        return newValue;
    }

    public boolean getCheckboxCurrentValue(int index) {
        return currentValues.get(index);
    }
}
