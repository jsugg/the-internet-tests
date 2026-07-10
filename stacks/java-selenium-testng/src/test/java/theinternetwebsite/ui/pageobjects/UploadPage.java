package theinternetwebsite.ui.pageobjects;

import java.nio.file.Paths;
import java.time.Duration;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import theinternetwebsite.ui.UITest;

public class UploadPage extends BasePage {
    @FindBy(how = How.XPATH, using = "//h3[normalize-space()='File Uploader']")
    private WebElement pageTitle;
    @FindBy(how = How.XPATH, using = "//a[normalize-space()='Elemental Selenium']")
    private WebElement pageFooterLink;
    @FindBy(how = How.XPATH, using = "//h3[normalize-space()='File Uploaded!']")
    private WebElement successPageTitle;
    @FindBy(how = How.XPATH, using = "//input[@id='file-submit']")
    private WebElement uploadButton;
    @FindBy(how = How.XPATH, using = "//input[@id='file-upload']")
    private WebElement chooseFileButton;
    @FindBy(how = How.XPATH, using = "//div[@id='drag-drop-upload']")
    private WebElement redBox;
    @FindBy(how = How.XPATH, using = "//div[@id='uploaded-files']")
    private WebElement uploadedFile;

    private final String fileName = "some-file.txt";

    public UploadPage(@NotNull UITest caller) {
        super(caller, "/upload");
    }

    @Override
    protected @NotNull WebElement pageTitle() {
        return pageTitle;
    }

    public void uploadFile() {
        String folder = "src/test/resources";
        String pathToFile = Paths.get(folder, this.fileName).toAbsolutePath().toString();
        chooseFileButton.sendKeys(pathToFile);
        this.uploadButton.click();
        waitFor(Duration.ofSeconds(30)).until(ExpectedConditions.visibilityOf(successPageTitle));
    }

    public boolean validateUploadedPageTitle() {
        return successPageTitle.getText().contains("File Uploaded!");
    }

    public boolean validateUploadedFileName() {
        return uploadedFile.getText().equals(fileName);
    }
}
