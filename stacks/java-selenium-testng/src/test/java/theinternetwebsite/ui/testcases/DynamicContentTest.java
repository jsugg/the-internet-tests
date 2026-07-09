// DynamicContentTest.java
package theinternetwebsite.ui.testcases;

import java.util.Map;
import java.util.HashMap;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import theinternetwebsite.ui.UITest;
import theinternetwebsite.ui.pageobjects.DynamicContentPage;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;

public class DynamicContentTest extends UITest {

    private DynamicContentPage dynamicContentPage;

    public DynamicContentTest() { }

    @BeforeMethod
    public void setup() {
        dynamicContentPage = new DynamicContentPage(this);
        Assert.assertTrue(dynamicContentPage.isPageOpen(), "Page not open");
    }

    @Test(description="Loads new texts and images on each page refresh", testName="UI-DYNCONTENT-001")
    public void loadNewContent() {
        HashMap<String, String> content, newContent;
        int testAttempts = 5;

        // Validate content change
        while (testAttempts > 0) {
            // Collect content
            content = dynamicContentPage.getContent(false);
            dynamicContentPage.reloadPage();
            newContent = dynamicContentPage.getContent(false);

            // Validate content differs 100%
            Assert.assertNotEquals(newContent, content);
            testAttempts--;
        }
    }

    @Test(description="Loads partially new content (text and images) on each page refresh", testName="UI-DYNCONTENT-002")
    public void loadPartiallyNewContent() {
        HashMap<String, String> content, newContent;
        int attempts = 10;
        boolean difference;

        // Validate content change
        while (attempts > 0) {
            difference = false;
            // Collect content
            content = dynamicContentPage.getContent(true);
            dynamicContentPage.reloadPage();
            newContent = dynamicContentPage.getContent(true);
            MapDifference<String, String> diff = Maps.difference(content, newContent);
            Map<String, MapDifference.ValueDifference<String>> entriesDiffering = diff.entriesDiffering();

            // Compare content
            if (entriesDiffering.size() > 0 || diff.entriesOnlyOnLeft().size() > 0 || diff.entriesOnlyOnRight().size() > 0)
                difference = true;
            // Validate content partially differs
            Assert.assertTrue(difference, "Content doesn't differ");
            attempts--;
        }
    }
}
