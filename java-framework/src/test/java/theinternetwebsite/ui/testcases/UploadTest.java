package theinternetwebsite.ui.testcases;

import org.testng.Assert;
import org.testng.annotations.Test;
import theinternetwebsite.ui.UITest;
import theinternetwebsite.ui.pageobjects.UploadPage;

public class UploadTest extends UITest {

    public UploadTest() { }

    @Test(description = "Upload a file", testName="UI-UPLOAD-001")
    public void fileUpload() {
        UploadPage uploadPage = new UploadPage(this);

        // Validate page loaded
        Assert.assertTrue(uploadPage.isPageOpen(), "Page not open");

        // Validate file uploaded
        uploadPage.uploadFile();
        Assert.assertTrue(uploadPage.validateUploadedPageTitle(), "'File uploaded' page not loaded");
        Assert.assertTrue(uploadPage.validateUploadedFileName(), "Uploaded filename not showing");
    }
}