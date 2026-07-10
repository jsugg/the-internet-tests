package theinternetwebsite.ui.testcases;

import theinternetwebsite.ui.UITest;
import theinternetwebsite.ui.pageobjects.DynamicLoadingPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DynamicLoadingTest extends UITest {

    private DynamicLoadingPage dynamicLoadingPage;

    public DynamicLoadingTest() { }

    @BeforeMethod
    public void setUp() {
        dynamicLoadingPage = new DynamicLoadingPage(this);
        dynamicLoadingPage.open();
    }

    @Test(description="Start button loads a message", testName="UI-DYNLOAD-001")
    public void loadMessage() {
        String expectedMessage = "Hello World!";
        String actualMessage;

        // Validate page loaded
        Assert.assertTrue(dynamicLoadingPage.isPageOpen(), "Page not open");

        // Validate the dynamically loaded element
        dynamicLoadingPage.startMessageRequest();
        actualMessage = dynamicLoadingPage.getSuccessMessage();
        Assert.assertEquals(expectedMessage, actualMessage);
    }
}
