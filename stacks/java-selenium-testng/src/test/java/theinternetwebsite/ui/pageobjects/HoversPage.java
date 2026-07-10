package theinternetwebsite.ui.pageobjects;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import theinternetwebsite.ui.UITest;

public class HoversPage extends BasePage {
    @FindBy(how = How.XPATH, using = "//h3[normalize-space()='Hovers']")
    private WebElement pageTitle;
    @FindBy(how = How.XPATH, using = "//div[@class='figure']")
    private List<WebElement> figures;

    public HoversPage(@NotNull UITest caller) {
        super(caller, "/hovers");
    }

    @Override
    public void open() {
        super.open();
        waitFor(Duration.ofSeconds(30)).until(d -> !this.getUsersDetails().isEmpty());
    }

    @Override
    protected @NotNull WebElement pageTitle() {
        return pageTitle;
    }

    public HashMap<String, String> getUsersDetails() {
        Actions builder = new Actions(driver());
        HashMap<String, String> userDetails = new HashMap<>();
        for (WebElement figure : this.figures) {
            builder.moveToElement(figure).perform();
            String photo = figure.findElement(By.tagName("img")).getAttribute("src");
            String username = figure.findElement(By.className("figcaption")).findElement(By.tagName("h5")).toString();
            userDetails.put(username, photo);
        }
        return userDetails;
    }

    public boolean validateUserDetails() {
        final int[] actual = {0};
        int expected = getUsersDetails().size();
        getUsersDetails().forEach((key, value) -> {
            if (this.validateHoverOverFigure(key, value)) {
                actual[0]++;
            }
        });
        return actual[0] == expected;
    }

    public boolean validateHoverOverFigure(String userName, String profilePhoto) {
        for (WebElement figure : this.figures) {
            boolean photosMatch = figure.findElement(By.tagName("img")).getAttribute("src").equals(profilePhoto);
            boolean userNameMatch = figure.findElement(By.className("figcaption")).findElement(By.tagName("h5")).toString().equals(userName);
            if (photosMatch && userNameMatch) {
                return true;
            }
        }
        return false;
    }

    public String getErrorPageTitleText() {
        return driver().findElement(By.xpath("//h2[contains(text(),\"Sinatra doesn't know this ditty.\")]")).getText();
    }

    public String getErrorPageUrl() {
        return caller().urlFor("/users/");
    }
}
