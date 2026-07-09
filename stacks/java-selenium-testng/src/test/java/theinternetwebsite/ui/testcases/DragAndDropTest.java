package theinternetwebsite.ui.testcases;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import theinternetwebsite.ui.UITest;
import theinternetwebsite.ui.pageobjects.DragAndDropPage;

public class DragAndDropTest extends UITest {

    private DragAndDropPage dragAndDropPage;
    final private String boxOneLetter = "A";
    final private String boxTwoLetter = "B";

    public DragAndDropTest() { }

    @BeforeMethod
    public void setup() {
        try {
            dragAndDropPage = new DragAndDropPage(this);
            // Validate page loaded
            Assert.assertTrue(dragAndDropPage.isPageOpen(), "Page not open");

            // Validate initial settings
            Assert.assertEquals(dragAndDropPage.getBoxLetter("boxOne"), boxOneLetter);
            Assert.assertEquals(dragAndDropPage.getBoxLetter("boxTwo"), boxTwoLetter);
        } catch (Exception e) {
            Assert.fail("An error occurred during setup: " + e.getMessage());
        }
    }

    @Test(description="Drags box A over box B", testName="UI-DRAGDROP-001")
    public void dragLeftToRight() {

        // Drag left box over right box
        dragAndDropPage.dragAndDropBox("boxA", "boxB");

        // Validate expected outcome
        Assert.assertEquals(dragAndDropPage.getBoxLetter("boxOne"), boxTwoLetter);
        Assert.assertEquals(dragAndDropPage.getBoxLetter("boxTwo"), boxOneLetter);
    }

    @Test(description="Drags box B over box A", testName="UI-DRAGDROP-002")
    public void dragRightToLeft() {

        // Drag left box over right box
        dragAndDropPage.dragAndDropBox("boxB", "boxA");

        // Validate expected outcome
        Assert.assertEquals(dragAndDropPage.getBoxLetter("boxOne"), boxTwoLetter);
        Assert.assertEquals(dragAndDropPage.getBoxLetter("boxTwo"), boxOneLetter);
    }

    @Test(description="Drags box A over box B, then box B over box A", testName="UI-DRAGDROP-003")
    public void dragBothWays() {

        // Drag left box over right box
        dragAndDropPage.dragAndDropBox("boxA", "boxB");

        // Validate expected outcome
        Assert.assertEquals(dragAndDropPage.getBoxLetter("boxOne"), boxTwoLetter);
        Assert.assertEquals(dragAndDropPage.getBoxLetter("boxTwo"), boxOneLetter);

        // Drag right box over left box
        dragAndDropPage.dragAndDropBox("boxB", "boxA");

        // Validate expected outcome
        Assert.assertEquals(dragAndDropPage.getBoxLetter("boxOne"), boxOneLetter);
        Assert.assertEquals(dragAndDropPage.getBoxLetter("boxTwo"), boxTwoLetter);
    }
}