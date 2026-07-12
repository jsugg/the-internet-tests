package theinternetwebsite.ui.testcases;

import java.util.List;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import theinternetwebsite.ui.UITest;
import theinternetwebsite.ui.pageobjects.ChallengingDomPage;

public class ChallengingDomTest extends UITest {
    private ChallengingDomPage challengingDomPage;

    @BeforeMethod
    public void setup() {
        challengingDomPage = new ChallengingDomPage(this);
        challengingDomPage.open();
        Assert.assertTrue(challengingDomPage.isPageOpen(), "Page not open");
    }

    @Test(
            description = "Validates stable content without dynamic identifiers",
            testName = "UI-CHALLENGING-001")
    public void validateStableContentWithoutDynamicIds() {
        Assert.assertEquals(challengingDomPage.buttonCount(), 3);
        Assert.assertTrue(challengingDomPage.isCanvasVisible(), "Canvas not visible");
        Assert.assertEquals(
                challengingDomPage.tableHeaders(),
                List.of("Lorem", "Ipsum", "Dolor", "Sit", "Amet", "Diceret", "Action"));
        Assert.assertEquals(challengingDomPage.tableRowCount(), 10);
        Assert.assertTrue(challengingDomPage.firstRowText().contains("Iuvaret0"));
        Assert.assertTrue(challengingDomPage.firstEditHref().endsWith("#edit"));
        Assert.assertTrue(challengingDomPage.firstDeleteHref().endsWith("#delete"));
    }
}
