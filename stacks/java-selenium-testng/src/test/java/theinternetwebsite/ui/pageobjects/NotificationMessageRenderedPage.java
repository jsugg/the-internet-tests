package theinternetwebsite.ui.pageobjects;

import java.time.Duration;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import theinternetwebsite.ui.UITest;

public class NotificationMessageRenderedPage extends BasePage {
    @FindBy(how = How.XPATH, using = "//h3[normalize-space()='Notification Message']")
    private WebElement pageTitle;
    @FindBy(how = How.XPATH, using = "//a[normalize-space()='Click here']")
    private WebElement link;
    @FindBy(how = How.XPATH, using = "//div[@id='flash']")
    private WebElement flashNotification;

    private final ArrayList<String> possibleMessages = new ArrayList<>();

    public NotificationMessageRenderedPage(@NotNull UITest caller) {
        super(caller, "/notification_message_rendered");
        this.possibleMessages.add("Action unsuccesful, please try again");
        this.possibleMessages.add("Action successful");
    }

    @Override
    protected @NotNull WebElement pageTitle() {
        return pageTitle;
    }

    public void clickOnLink() {
        waitFor(Duration.ofSeconds(30)).until(ExpectedConditions.elementToBeClickable(this.link)).click();
    }

    public String getFlashMessage() {
        this.clickOnLink();
        waitFor(Duration.ofSeconds(30)).until(ExpectedConditions.visibilityOf(flashNotification));
        return UITest.cleanTextContent(flashNotification.getText().trim());
    }

    public boolean validateFlashMessages() {
        String currentMessage = this.getFlashMessage();
        this.clickOnLink();
        int retries = this.possibleMessages.size();

        while (retries > 0) {
            for (String message : this.possibleMessages) {
                if (message.trim().contains(currentMessage.trim())) {
                    retries--;
                }
            }
            currentMessage = this.getFlashMessage();
            waitFor(Duration.ofSeconds(30)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='flash']")));
        }
        return true;
    }
}
