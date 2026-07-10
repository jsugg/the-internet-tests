// DropdownTest.java
package theinternetwebsite.ui.testcases;

import java.util.List;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import theinternetwebsite.ui.UITest;
import theinternetwebsite.ui.pageobjects.DropdownPage;

public class DropdownTest extends UITest {

    private DropdownPage dropdownPage;

    public DropdownTest() { }

    @BeforeMethod
    public void setup() {
        dropdownPage = new DropdownPage(this);
        dropdownPage.open();
        Assert.assertTrue(dropdownPage.isPageOpen(), "Page not open");
    }

    @Test(description="Dropdown options can be selected", testName="UI-DROPDOWN-001")
    public void selectAllOptions() {
        List<String> options = dropdownPage.getDropdownOptions();

        // Validate selectable options
        for (String option : options) {
            dropdownPage.setDropdownOption(option);
            Assert.assertTrue(dropdownPage.isDropdownOptionSelected(option), "Dropdown option not selected");
        }
    }
}
