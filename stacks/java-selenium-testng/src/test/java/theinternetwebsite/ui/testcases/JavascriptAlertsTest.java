package theinternetwebsite.ui.testcases;

import org.testng.Assert;
import org.testng.annotations.Test;
import theinternetwebsite.ui.UITest;
import theinternetwebsite.ui.pageobjects.JavascriptAlertsPage;

public class JavascriptAlertsTest extends UITest {

    public JavascriptAlertsTest() { }

    @Test(description="Popups appear on button click", testName="UI-JSALERT-001")
    public void popupDisplay() {
        JavascriptAlertsPage javascriptAlertsPage = new JavascriptAlertsPage(this);

        // Validate page loaded
        Assert.assertTrue(javascriptAlertsPage.isPageOpen(), "Page not open");

        // Validate popup displayed
        Assert.assertTrue(javascriptAlertsPage.validatePopupsDisplay(true), "Popup(s) didn't appear as expected");
    }

    @Test(description="Popups messages are as expected", testName="UI-JSALERT-002")
    public void popupMessages() {
        JavascriptAlertsPage javascriptAlertsPage = new JavascriptAlertsPage(this);

        // Validate page loaded
        Assert.assertTrue(javascriptAlertsPage.isPageOpen(), "Page not open");

        // Validate expected popup messages
        Assert.assertTrue(javascriptAlertsPage.validatePopupsDisplay(false), "Popup(s) aren't showing the expected messages");
    }

    @Test(description="Popup's input field works as expected", testName="UI-JSALERT-003")
    public void popupInputField() {
        JavascriptAlertsPage javascriptAlertsPage = new JavascriptAlertsPage(this);

        // Validate page loaded
        Assert.assertTrue(javascriptAlertsPage.isPageOpen(), "Page not open");

        // Validate popup's input field expected behaviour
        Assert.assertTrue(javascriptAlertsPage.validatePopupInputField(), "Input field is not working as expected");
    }
}