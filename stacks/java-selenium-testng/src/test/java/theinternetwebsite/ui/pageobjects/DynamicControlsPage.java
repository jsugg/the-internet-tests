package theinternetwebsite.ui.pageobjects;

import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import theinternetwebsite.ui.UITest;

public class DynamicControlsPage extends BasePage {
    @FindBy(how = How.XPATH, using = "//h4[normalize-space()='Dynamic Controls']")
    private WebElement pageTitle;
    @FindBy(how = How.XPATH, using = "//input[@type='text']")
    private WebElement textBox;
    @FindBy(how = How.XPATH, using = "//input[@type='checkbox']")
    private WebElement checkBox;
    @FindBy(how = How.XPATH, using = "//button[text()= 'Enable']")
    private WebElement enableButton;
    @FindBy(how = How.XPATH, using = "//button[text()= 'Disable']")
    private WebElement disableButton;
    @FindBy(how = How.XPATH, using = "//button[text()= 'Add']")
    private WebElement addButton;
    @FindBy(how = How.XPATH, using = "//button[text()= 'Remove']")
    private WebElement removeButton;

    public DynamicControlsPage(@NotNull UITest caller) {
        super(caller, "/dynamic_controls");
    }

    @Override
    protected @NotNull WebElement pageTitle() {
        return pageTitle;
    }

    public void reloadPage() {
        UITest.reloadPage(driver());
        waitUntilOpen();
    }

    public void enableTextBox() {
        enableButton.click();
        defaultWait().until(ExpectedConditions.elementToBeClickable(disableButton));
        defaultWait().until(ExpectedConditions.elementToBeClickable(textBox)).isEnabled();
    }

    public void disableTextBox() {
        disableButton.click();
        defaultWait().until(ExpectedConditions.elementToBeClickable(enableButton));
        defaultWait().until(ExpectedConditions.not(ExpectedConditions.elementToBeClickable(textBox)));
    }

    public void addCheckboxButton() {
        addButton.click();
        defaultWait().until(ExpectedConditions.elementToBeClickable(removeButton));
        defaultWait().until(ExpectedConditions.visibilityOf(checkBox));
    }

    public void removeCheckboxButton() {
        removeButton.click();
        defaultWait().until(ExpectedConditions.elementToBeClickable(addButton));
        defaultWait().until(ExpectedConditions.invisibilityOf(checkBox));
    }

    public void clickCheckbox() {
        boolean currentState = this.isCheckboxSelected();
        checkBox.click();
        defaultWait().until(ExpectedConditions.elementSelectionStateToBe(checkBox, !currentState));
    }

    public boolean isCheckboxSelected() {
        return checkBox.isSelected();
    }

    public boolean isCheckboxEnabled() {
        return !driver().findElements(By.xpath("//input[@type='checkbox']")).isEmpty();
    }

    public boolean isTextboxEnabled() {
        return textBox.isEnabled();
    }
}
