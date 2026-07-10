package theinternetwebsite.ui.pageobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import theinternetwebsite.ui.UITest;

public class DynamicContentPage extends BasePage {
    @FindBy(how = How.XPATH, using = "//h3[normalize-space()='Dynamic Content']")
    private WebElement pageTitle;
    @FindBy(how = How.CSS, using = "[href][text()='click here']")
    private WebElement clickHereLink;
    @FindBy(how = How.CSS, using = ".large-10.columns")
    private WebElement dynamicText;
    @FindBy(how = How.CSS, using = ".large-2.columns")
    private WebElement dynamicImages;

    public DynamicContentPage(@NotNull UITest caller) {
        super(caller, "/dynamic_content");
    }

    @Override
    protected @NotNull WebElement pageTitle() {
        return pageTitle;
    }

    public void reloadPage() {
        UITest.reloadPage(driver());
        waitUntilOpen();
    }

    public HashMap<String, String> getContent(@NotNull Boolean partial) {
        String staticContentQueryString = "?with_content=static";
        driver().get(Boolean.TRUE.equals(partial) ? pageUrl() + staticContentQueryString : pageUrl());
        waitUntilOpen();

        List<WebElement> rawImages = driver().findElements(By.xpath("(//*[@class='large-2 columns'])"));
        List<WebElement> rawTexts = driver().findElements(By.xpath("(//*[@class='large-10 columns'])"));
        List<String> tempList = new ArrayList<>();
        HashMap<String, String> content = new HashMap<>();

        for (WebElement image : rawImages) {
            tempList.add(image.findElement(By.tagName("img")).getAttribute("src"));
        }
        int index = 0;
        for (WebElement text : rawTexts) {
            content.put(tempList.get(index), text.getText());
            index++;
        }
        return content;
    }
}
