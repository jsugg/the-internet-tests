package theinternetwebsite.ui.testcases;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import theinternetwebsite.ui.UITest;
import theinternetwebsite.ui.pageobjects.DynamicControlsPage;

public class DynamicControlsTest extends UITest {

    private DynamicControlsPage dynamicControlsPage;

    @BeforeMethod
    public void setUp() {
        dynamicControlsPage = new DynamicControlsPage(this);
        dynamicControlsPage.open();
    }

    @Test(description="Removes and adds a checkbox", testName="UI-DYNCTRL-001")
    public void checkboxTest() {
        int testAttempts = 3;

        // Validate page loaded
        Assert.assertTrue(dynamicControlsPage.isPageOpen(), "Page not open");

        // Validates checkbox add/remove behaviour
        while (testAttempts > 0) {
            // Remove checkbox
            dynamicControlsPage.removeCheckboxButton();
            // Validate removed checkbox
            Assert.assertFalse(dynamicControlsPage.isCheckboxEnabled(), "Checkbox is enabled");
            // Add checkbox
            dynamicControlsPage.addCheckboxButton();
            // Validate added checkbox
            Assert.assertTrue(dynamicControlsPage.isCheckboxEnabled(), "Checkbox is disabled");
            testAttempts--;
        }

        // Same as above but reloading the page
        testAttempts = 2;
        while (testAttempts > 0) {
            // Remove checkbox
            dynamicControlsPage.removeCheckboxButton();
            // Validate removed checkbox
            Assert.assertFalse(dynamicControlsPage.isCheckboxEnabled(), "Checkbox is enabled");
            // Add checkbox
            dynamicControlsPage.addCheckboxButton();
            // Validate added checkbox
            Assert.assertTrue(dynamicControlsPage.isCheckboxEnabled(), "Checkbox is disabled");
            // Reload the page
            dynamicControlsPage.reloadPage();
            testAttempts--;
        }
    }

    @Test(description = "Toggles the checkbox on and off", testName="UI-DYNCTRL-002")
    public void toggleCheckbox() {
        // Validate page loaded
        Assert.assertTrue(dynamicControlsPage.isPageOpen(), "Page not open");
        // Validate checkbox toggle behaviour
        boolean currentState = dynamicControlsPage.isCheckboxSelected();
        dynamicControlsPage.clickCheckbox();
        Assert.assertEquals(dynamicControlsPage.isCheckboxSelected(), !currentState);
        dynamicControlsPage.clickCheckbox();
        Assert.assertEquals(dynamicControlsPage.isCheckboxSelected(), currentState);
    }

    @Test(description = "Enables and disables the textbox", testName="UI-DYNCTRL-003")
    public void textboxTest() {
        // Validate page loaded
        Assert.assertTrue(dynamicControlsPage.isPageOpen(), "Page not open");
        // Validate textbox enabled
        dynamicControlsPage.enableTextBox();
        Assert.assertTrue(dynamicControlsPage.isTextboxEnabled(), "Textbox is disabled");
        // Validate textbox disabled
        dynamicControlsPage.disableTextBox();
        Assert.assertFalse(dynamicControlsPage.isTextboxEnabled(), "Textbox is enabled");
    }
}
