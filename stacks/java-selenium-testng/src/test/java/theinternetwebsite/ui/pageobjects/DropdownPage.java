// DropdownPage.java
package theinternetwebsite.ui.pageobjects;

import theinternetwebsite.ui.UITest;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import java.util.ArrayList;
import java.util.List;

public class DropdownPage {
    @FindBy(css = "h3")
    public WebElement pageTitle;
    @FindBy(tagName = "select")
    public WebElement dropdown;
    private final UITest caller;
    private final Select dropdwn;
    List<WebElement> dropdownOptions;
    private final String pageUrl;

    public DropdownPage(UITest caller) {
        this.caller = caller;
        this.pageUrl = this.caller.getBaseUrl() + "/dropdown";
        try {
            this.caller.getDriver().get(this.pageUrl);
            PageFactory.initElements(this.caller.getDriver(), this);
            this.caller.pageFactoryInitWait(pageTitle);
            this.dropdwn = new Select(this.dropdown);
            this.dropdownOptions = this.dropdwn.getOptions();
        } catch (NoSuchElementException e) {
            throw new RuntimeException("Could not initialize the DropdownPage: " + e.getMessage());
        }
    }

    public Boolean isPageOpen() { return this.caller.isPageOpen(this.pageUrl, this.pageTitle); }

    public List<String> getDropdownOptions() {
        List<String> dropdwnOptions = new ArrayList<>();
        for (WebElement webElement : this.dropdownOptions) {
            if (webElement.getAttribute("disabled") == null) dropdwnOptions.add(webElement.getText());
        }
        return dropdwnOptions;
    }

    public void setDropdownOption(String option) {
        this.dropdwn.selectByVisibleText(option);
    }

    public Boolean isDropdownOptionSelected(String option) { return this.dropdwn.getFirstSelectedOption().getText().equals(option); }
}
