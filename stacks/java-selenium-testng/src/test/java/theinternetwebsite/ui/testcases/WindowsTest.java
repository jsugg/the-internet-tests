package theinternetwebsite.ui.testcases;

import org.testng.Assert;
import org.testng.annotations.Test;
import theinternetwebsite.ui.UITest;
import theinternetwebsite.ui.pageobjects.WindowsPage;

public class WindowsTest extends UITest {

    public WindowsTest() { }

    @Test(description = "Click on link; see a new tab with the expected message", testName="UI-WINDOWS-001")
    public void validateNewTab() {
        WindowsPage windowsPage = new WindowsPage(this);
        windowsPage.open();
        // Validate page loaded
        Assert.assertTrue(windowsPage.isPageOpen(), "Page not open");
        // Validate expected message in new tab
        Assert.assertTrue(windowsPage.validateNewTab(), "Couldn't validate the new message and/or the new tab");
    }
}
