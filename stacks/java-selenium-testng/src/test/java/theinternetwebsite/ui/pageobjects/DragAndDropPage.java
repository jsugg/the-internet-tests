package theinternetwebsite.ui.pageobjects;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import theinternetwebsite.ui.UITest;

public class DragAndDropPage extends BasePage {
    @FindBy(css = "h3")
    private WebElement pageTitle;
    @FindBy(css = "#column-a")
    private WebElement boxA;
    @FindBy(css = "#column-a header")
    private WebElement boxOne;
    @FindBy(css = "#column-b")
    private WebElement boxB;
    @FindBy(css = "#column-b header")
    private WebElement boxTwo;

    private final Map<String, WebElement> boxes = new HashMap<>();
    private final Map<String, WebElement> letters = new HashMap<>();

    public DragAndDropPage(@NotNull UITest caller) {
        super(caller, "/drag_and_drop");
        this.boxes.put("boxA", this.boxA);
        this.boxes.put("boxB", this.boxB);
        this.letters.put("boxOne", this.boxOne);
        this.letters.put("boxTwo", this.boxTwo);
    }

    @Override
    protected @NotNull WebElement pageTitle() {
        return pageTitle;
    }

    public void dragAndDropBox(String source, String destination) {
        WebElement sourceBox = Objects.requireNonNull(this.boxes.get(source), "Unknown source box: " + source);
        WebElement destinationBox = Objects.requireNonNull(this.boxes.get(destination), "Unknown destination box: " + destination);
        waitFor(Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOf(sourceBox));
        waitFor(Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOf(destinationBox));
        caller().dragAndDropJS(sourceBox, destinationBox);
    }

    public String getBoxLetter(String box) {
        WebElement letter = Objects.requireNonNull(this.letters.get(box), "Unknown box letter: " + box);
        waitFor(Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOf(letter));
        return letter.getText();
    }
}
