package theinternetwebsite.ui.testcases;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import theinternetwebsite.ui.UITest;
import theinternetwebsite.ui.pageobjects.FloatingMenuPage;

public class FloatingMenuTest extends UITest {

    private FloatingMenuPage floatingMenuPage;

    @BeforeMethod
    public void setUp() {
        this.floatingMenuPage = new FloatingMenuPage(this);
    }

    @Test(description = "Scrolls down the page and the menu is still there", testName="UI-FLOATING-001")
    public void scrollTest() {
        // Validate page loaded
        Assert.assertTrue(this.floatingMenuPage.isPageOpen(), "Page not open");

        // Move
        String initialPosition = this.floatingMenuPage.getMenuPosition();
        this.floatingMenuPage.scrollToBottom();
        String finalPosition = this.floatingMenuPage.getMenuPosition();

        // Validate menu behaviour
        Assert.assertTrue(this.floatingMenuPage.validateMenuVisibility(), "Menu is not visible");
        Assert.assertNotEquals(initialPosition, finalPosition, "Menu position didn't change");
    }
}
