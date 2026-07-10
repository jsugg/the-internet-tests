package theinternetwebsite.ui.testcases;

import org.testng.Assert;
import org.testng.annotations.Test;
import theinternetwebsite.ui.UITest;
import theinternetwebsite.ui.pageobjects.IFramePage;

public class IFrameTest extends UITest {

    public IFrameTest() { }

    @Test(description = "Switches between contexts and performs operations", testName="UI-IFRAME-001")
    public void testContextSwitching() {
        IFramePage iFramePage = new IFramePage(this);
        iFramePage.open();
        String textBucket = "Test in progress...Your content goes here.Your content goes here.", aux;

        // Validate page loaded
        Assert.assertTrue(iFramePage.isPageOpen(), "Page not open");

        // Write to the iframe's context textarea
        iFramePage.switchToIFrame();
        aux = iFramePage.getIFrameTextAreaText();
        iFramePage.writeIFrameTextAreaText(textBucket);
        // Validate expected text
        Assert.assertEquals(iFramePage.getIFrameTextAreaText(), aux + textBucket, "Actual text doesn't match expected text");

        // Access the main page's context page title
        aux = this.getDriver().toString();
        iFramePage.switchToMain();
        textBucket = iFramePage.getPageTitle();
        // Validate context elements reachable
        Assert.assertEquals(iFramePage.getPageTitle(), textBucket, "Page title doesn't match the previous value");

        // Validate context changed
        Assert.assertEquals(aux, this.getDriver().toString(), "Context didn't change");

        // Write to the textarea
        aux = this.getDriver().toString();
        iFramePage.switchToIFrame();
        iFramePage.clearIFrameTextAreaText();
        iFramePage.writeIFrameTextAreaText(textBucket);
        // Validate expected text
        Assert.assertEquals(iFramePage.getIFrameTextAreaText(), textBucket, "Textarea text doesn't match the expected text");

        // Validate context changed
        Assert.assertEquals(aux, this.getDriver().toString(), "Context didn't change");

        // Clear the textarea
        iFramePage.clearIFrameTextAreaText();
        // Validate area cleared
        Assert.assertTrue(iFramePage.getIFrameTextAreaText().isEmpty(), "Textarea should be empty");

        // Write to the textarea
        textBucket = iFramePage.getIFrameTextAreaText();
        textBucket = new StringBuilder(textBucket).reverse().toString();
        iFramePage.writeIFrameTextAreaText(textBucket);
        // Validate expected text
        Assert.assertEquals(iFramePage.getIFrameTextAreaText(), textBucket, "Textarea text doesn't match the expected text");
    }
}
