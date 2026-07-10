package theinternetwebsite.ui.pageobjects;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import theinternetwebsite.ui.UITest;

public class DropdownPage extends BasePage {
    @FindBy(css = "h3")
    private WebElement pageTitle;

    @FindBy(tagName = "select")
    private WebElement dropdown;

    public DropdownPage(@NotNull UITest caller) {
        super(caller, "/dropdown");
    }

    @Override
    protected @NotNull WebElement pageTitle() {
        return pageTitle;
    }

    public List<String> getDropdownOptions() {
        List<String> dropdownOptions = new ArrayList<>();
        for (WebElement webElement : dropdownSelect().getOptions()) {
            if (webElement.getAttribute("disabled") == null) {
                dropdownOptions.add(webElement.getText());
            }
        }
        return dropdownOptions;
    }

    public void setDropdownOption(String option) {
        dropdownSelect().selectByVisibleText(option);
    }

    public boolean isDropdownOptionSelected(String option) {
        return dropdownSelect().getFirstSelectedOption().getText().equals(option);
    }

    private Select dropdownSelect() {
        return new Select(this.dropdown);
    }
}
