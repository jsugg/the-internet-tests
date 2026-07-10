package theinternetwebsite.ui.testcases;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import theinternetwebsite.ui.UITest;
import theinternetwebsite.ui.pageobjects.DownloadPage;

public class DownloadTest extends UITest {

    public DownloadTest() {}

    @Test(description = "Downloads a file and compares it with what's expected", testName="UI-DOWNLOAD-001")
    public void fileDownload() {
/*        if (this.getCurrentBrowser().equals("remote-chrome")) {
            throw new SkipException("Skipping this test case; Github Actions doesn't support it yet");
        }*/

        // Validate page loaded
        DownloadPage downloadPage = new DownloadPage(this);
        downloadPage.open();
        Assert.assertTrue(downloadPage.isPageOpen(), "Page not open");

        // Validate file download success
        Assert.assertTrue(downloadPage.fileDownload());
    }
}
