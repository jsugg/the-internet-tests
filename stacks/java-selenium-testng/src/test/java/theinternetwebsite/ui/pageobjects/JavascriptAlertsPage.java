package theinternetwebsite.ui.pageobjects;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import theinternetwebsite.ui.UITest;

public class JavascriptAlertsPage extends BasePage {
    @FindBy(how = How.XPATH, using = "//h3[normalize-space()='JavaScript Alerts']")
    private WebElement pageTitle;
    @FindBy(how = How.XPATH, using = "//button")
    private List<WebElement> buttons;
    @FindBy(how = How.XPATH, using = "//p[@id='result']")
    private WebElement resultTextMessage;

    private final ArrayList<String> popupMessages = new ArrayList<>();
    private String mainWindow;

    public JavascriptAlertsPage(@NotNull UITest caller) {
        super(caller, "/javascript_alerts");
        popupMessages.add("I am a JS Alert");
        popupMessages.add("I am a JS Confirm");
        popupMessages.add("I am a JS prompt");
    }

    @Override
    public void open() {
        super.open();
        this.mainWindow = driver().getWindowHandle();
    }

    @Override
    protected @NotNull WebElement pageTitle() {
        return pageTitle;
    }

    public boolean validatePopupsDisplay(Boolean skipMessages) {
        Actions builder = new Actions(driver());
        int i = 0;

        for (WebElement button : this.buttons) {
            waitFor(Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOf(button));
            waitFor(Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(button));
            builder.moveToElement(button).click(button).perform();
            waitFor(Duration.ofSeconds(10)).until(ExpectedConditions.alertIsPresent());

            Alert alertPopup = driver().switchTo().alert();
            String popUpMessage = alertPopup.getText();
            if (!Boolean.TRUE.equals(skipMessages) && !popUpMessage.equals(popupMessages.get(i))) {
                alertPopup.dismiss();
                return false;
            }
            alertPopup.dismiss();
            driver().switchTo().window(this.mainWindow);
            i++;
        }
        return true;
    }

    public boolean validatePopupInputField() {
        Actions builder = new Actions(driver());
        String inputMessage = "This is a test message";
        WebElement buttonToBeTested = this.buttons.get(2);

        waitFor(Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOf(buttonToBeTested));
        waitFor(Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(buttonToBeTested));
        builder.moveToElement(buttonToBeTested).click(buttonToBeTested).perform();

        Alert alertPopup = driver().switchTo().alert();
        alertPopup.sendKeys(inputMessage);
        alertPopup.accept();
        driver().switchTo().window(this.mainWindow);
        return resultTextMessage.getText().equals("You entered: " + inputMessage);
    }
}
