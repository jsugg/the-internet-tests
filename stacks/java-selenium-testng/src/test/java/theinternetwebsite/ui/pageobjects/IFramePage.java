package theinternetwebsite.ui.pageobjects;

import java.time.Duration;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import theinternetwebsite.ui.UITest;

public class IFramePage extends BasePage {
    @FindBy(how = How.XPATH, using = "//h3[normalize-space()='An iFrame containing the TinyMCE WYSIWYG Editor']")
    private WebElement pageTitle;
    @FindBy(how = How.XPATH, using = "//*[@id='mce_0_ifr']")
    private WebElement iFrame;
    @FindBy(how = How.XPATH, using = "//*[@id='tinymce']")
    private WebElement iFrameTextArea;

    public IFramePage(@NotNull UITest caller) {
        super(caller, "/iframe");
    }

    @Override
    protected @NotNull WebElement pageTitle() {
        return pageTitle;
    }

    public void switchToIFrame() {
        driver().switchTo().frame(this.iFrame);
        // TinyMCE injects its default content asynchronously after the frame loads.
        // Wait until it has rendered so reads don't race the editor boot: the test
        // reads the text and writeIFrameTextAreaText re-reads it, and both snapshots
        // must agree. (The tinymce global lives in the parent document, not this
        // content frame, so probe the rendered body text instead.)
        waitFor(Duration.ofSeconds(10)).until(d -> !iFrameTextArea.getText().isEmpty());
    }

    public void switchToMain() {
        driver().switchTo().defaultContent();
    }

    public String getPageTitle() {
        return this.pageTitle.getText();
    }

    public String getIFrameTextAreaText() {
        return this.iFrameTextArea.getText();
    }

    public void writeIFrameTextAreaText(String text) {
        String expectedText = this.iFrameTextArea.getText() + text;
        ((JavascriptExecutor) driver()).executeScript(
                "arguments[0].textContent = arguments[1];",
                this.iFrameTextArea,
                expectedText);
        waitFor(Duration.ofSeconds(10)).until(ExpectedConditions.textToBePresentInElement(iFrameTextArea, expectedText));
    }

    public void clearIFrameTextAreaText() {
        ((JavascriptExecutor) driver()).executeScript(
                "arguments[0].textContent = '';",
                this.iFrameTextArea);
        waitFor(Duration.ofSeconds(10)).until(d -> iFrameTextArea.getText().isEmpty());
    }
}
