package theinternetwebsite.ui.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import theinternetwebsite.ui.UITest;
import java.util.ArrayList;
import java.util.List;

public class CheckboxesPage {

    private final UITest caller;
    private List<Boolean> currentValues = new ArrayList<>();
    private List<WebElement> checkboxes;

    private final By checkboxesLocator = By.cssSelector("input[type='checkbox']");

    public CheckboxesPage(UITest caller) {
        this.caller = caller;
        this.checkboxes = this.caller.getDriver().findElements(By.cssSelector("input[type='checkbox']"));
    }

    public boolean isPageOpen() {
        this.caller.getDriver().get(this.caller.getBaseUrl() + "/checkboxes");
        return this.caller.getDriver().getTitle().equals("The Internet");
    }

    public List<Boolean> getAllCheckboxCurrentValues() {
        this.checkboxes = this.caller.getDriver().findElements(checkboxesLocator);
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
        return currentValues.get(index)? currentValues.get(index) : false;
    }
}
