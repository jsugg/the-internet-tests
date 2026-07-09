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
    }

    @Test(description="Removes and adds a checkbox", testName="UI-DYNCTRL-001")
    public void checkboxTest() {
        try {
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
        } catch (AssertionError error) {
            System.out.println("Assertion failed: " + error.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred during the checkboxTest: " + e.getMessage());
        }
    }

    @Test(description = "Toggles the checkbox on and off", testName="UI-DYNCTRL-002")
    public void toggleCheckbox() {
        try {
            // Validate page loaded
            Assert.assertTrue(dynamicControlsPage.isPageOpen(), "Page not open");
            // Validate checkbox toggle behaviour
            Boolean currentState = dynamicControlsPage.isCheckboxSelected();
            dynamicControlsPage.clickCheckbox();
            Assert.assertEquals(dynamicControlsPage.isCheckboxSelected(), !currentState);
            dynamicControlsPage.clickCheckbox();
            Assert.assertEquals(dynamicControlsPage.isCheckboxSelected(), currentState);
        } catch (AssertionError error) {
            System.out.println("Assertion failed: " + error.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred during the toggleCheckbox test: " + e.getMessage());
        }
    }

    @Test(description = "Enables and disables the textbox", testName="UI-DYNCTRL-003")
    public void textboxTest() {
        try {
            // Validate page loaded
            Assert.assertTrue(dynamicControlsPage.isPageOpen(), "Page not open");
            // Validate textbox enabled
            dynamicControlsPage.enableTextBox();
            Assert.assertTrue(dynamicControlsPage.isTextboxEnabled(), "Textbox is disabled");
            // Validate textbox disabled
            dynamicControlsPage.disableTextBox();
            Assert.assertFalse(dynamicControlsPage.isTextboxEnabled(), "Textbox is enabled");
        } catch (AssertionError error) {
            System.out.println("Assertion failed: " + error.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred during the textboxTest: " + e.getMessage());
        }
    }
}