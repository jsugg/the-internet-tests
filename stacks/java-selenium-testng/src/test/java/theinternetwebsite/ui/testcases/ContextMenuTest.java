package theinternetwebsite.ui.testcases;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;
import theinternetwebsite.ui.UITest;
import theinternetwebsite.ui.pageobjects.ContextMenuPage;

public class ContextMenuTest extends UITest {

    public ContextMenuTest() {}

    @Test(description = "Expected alert popup opens on right click over box", testName="UI-CONTEXT-001")
    public void triggerPopupOnContextClick() {
        if (this.getCurrentBrowser().equals("remote-chrome")) {
            throw new SkipException("Skipping this test case; Github Actions doesn't support it yet");
        }

        ContextMenuPage contextMenuPage = new ContextMenuPage(this);
        contextMenuPage.open();

        // Validate page loaded
        Assert.assertTrue(contextMenuPage.isPageOpen(), "Page not open");

        // Validate popup opens
        String expectedPopupText = "You selected a context menu";
        contextMenuPage.rightClickBox();
        String actualPopupText = contextMenuPage.getAlertPopupText();
        Assert.assertEquals(actualPopupText, expectedPopupText);
    }
}
