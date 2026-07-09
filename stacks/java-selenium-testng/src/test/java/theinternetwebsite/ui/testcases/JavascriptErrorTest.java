package theinternetwebsite.ui.testcases;

import org.testng.Assert;
import org.testng.annotations.Test;
import theinternetwebsite.ui.UITest;
import theinternetwebsite.ui.pageobjects.JavascriptErrorPage;

public class JavascriptErrorTest extends UITest {

    public JavascriptErrorTest() { }

    @Test(description = "We see the page with the right error message", testName="UI-JSERROR-001")
    public void validateErrorMessage() {
        JavascriptErrorPage javascriptErrorPage = new JavascriptErrorPage(this);

        // Validate page loaded
        Assert.assertTrue(javascriptErrorPage.isPageOpen(), "Page not open");

        // Validate expected error message
        Assert.assertTrue(javascriptErrorPage.validateErrorMessage(), "Message was not expected");
    }
}