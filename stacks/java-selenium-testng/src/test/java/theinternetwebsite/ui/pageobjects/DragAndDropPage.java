package theinternetwebsite.ui.pageobjects;

import org.openqa.selenium.NoSuchElementException;
import theinternetwebsite.ui.UITest;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.HashMap;

public class DragAndDropPage {
    @FindBy(css = "h3")
    public WebElement pageTitle;
    @FindBy(css = "#column-a")
    public WebElement boxA;
    @FindBy(css = "#column-a header")
    public WebElement boxOne;
    @FindBy(css = "#column-b")
    public WebElement boxB;
    @FindBy(css = "#column-b header")
    public WebElement boxTwo;
    private final UITest caller;
    private final HashMap<String, WebElement> boxes = new HashMap<>();
    private final HashMap<String, WebElement> letters = new HashMap<>();
    private final String pageUrl;

    public DragAndDropPage(UITest caller) {
        this.caller = caller;
        this.pageUrl = this.caller.getBaseUrl() + "/drag_and_drop";
        this.caller.getDriver().get(this.pageUrl);
        PageFactory.initElements(this.caller.getDriver(), this);
        this.caller.pageFactoryInitWait(pageTitle);
        this.boxes.put("boxA", this.boxA);
        this.boxes.put("boxB", this.boxB);
        this.letters.put("boxOne", this.boxOne);
        this.letters.put("boxTwo", this.boxTwo);
    }

    public Boolean isPageOpen() {
        try {
            return this.caller.isPageOpen(this.pageUrl, this.pageTitle);
        } catch (Exception e) {
            System.out.println("An error occurred while checking if the page is open: " + e.getMessage());
            return false;
        }
    }

    public void dragAndDropBox(String source, String destination) {
        try {
            WebDriverWait wait = new WebDriverWait(this.caller.getDriver(), Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(this.boxes.get(source)));
            wait.until(ExpectedConditions.visibilityOf(this.boxes.get(destination)));
            caller.dragAndDropJS(this.boxes.get(source), this.boxes.get(destination));
        } catch (NoSuchElementException e) {
            System.out.println("One of the elements was not found: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred while dragging and dropping: " + e.getMessage());
        }
    }

    public String getBoxLetter(String box) {
        try {
            WebDriverWait wait = new WebDriverWait(this.caller.getDriver(), Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(this.letters.get(box)));
            return this.letters.get(box).getText();
        } catch (NoSuchElementException e) {
            System.out.println("The element was not found: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.out.println("An error occurred while getting the box letter: " + e.getMessage());
            return null;
        }
    }
}
