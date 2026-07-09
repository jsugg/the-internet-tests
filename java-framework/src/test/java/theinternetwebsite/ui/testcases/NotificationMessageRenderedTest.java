package theinternetwebsite.ui.testcases;

import org.testng.Assert;
import org.testng.annotations.Test;
import theinternetwebsite.ui.UITest;
import theinternetwebsite.ui.pageobjects.NotificationMessageRenderedPage;

public class NotificationMessageRenderedTest extends UITest {

    public NotificationMessageRenderedTest() { }

    @Test(description="Validates messages shown inside the flash box", testName="UI-NOTIFY-001")
    public void validateFlashMessages() {
        NotificationMessageRenderedPage notificationMessageRenderedPage = new NotificationMessageRenderedPage(this);

        // Validate page loaded
        Assert.assertTrue(notificationMessageRenderedPage.isPageOpen(), "Page not open");

        // Validate expected messages displayed
        Assert.assertTrue(notificationMessageRenderedPage.validateFlashMessages(), "Messages couldn't be validated");
    }
}