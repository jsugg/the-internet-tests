package theinternetwebsite.ui.testcases;

import org.testng.Assert;
import org.testng.annotations.Test;
import theinternetwebsite.ui.UITest;
import theinternetwebsite.ui.pageobjects.CheckboxesPage;

import java.util.List;

public class CheckboxesTest extends UITest {

    @Test(description = "Checkboxes can be checked and unchecked", testName="UI-CHECKBOX-001")
    public void testCheckboxChecking() {
        CheckboxesPage checkboxesPage = new CheckboxesPage(this);
        checkboxesPage.open();

        // Validate page loaded
        Assert.assertTrue(checkboxesPage.isPageOpen(), "Page not open");

        // All checkboxes toggled back and forth
        List<Boolean> currentStatuses = checkboxesPage.getAllCheckboxCurrentValues();
        for (int i = 0; i < currentStatuses.size(); i++) {
            // Toggle/untoggle the checkbox
            boolean prevStatus = currentStatuses.get(i);
            boolean isChecked = checkboxesPage.toggleCheckbox(i);
            boolean currentStatus = checkboxesPage.getCheckboxCurrentValue(i);
            Assert.assertEquals(currentStatus, isChecked);

            // Toggle/untoggle the checkbox back
            checkboxesPage.toggleCheckbox(i);
            currentStatus = checkboxesPage.getCheckboxCurrentValue(i);
            Assert.assertEquals(currentStatus, prevStatus);
        }
    }
}
