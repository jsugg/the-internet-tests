package theinternetwebsite.ui.support;

import static org.testng.Assert.assertEquals;

import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicInteger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

public final class BrowserWaitsTest {
    @Test(description = "Waits for a footer that is absent on the initial poll")
    public void waitForPageFactoryElementsWaitsForDelayedFooter() {
        AtomicInteger footerLookups = new AtomicInteger();
        WebElement visibleElement = visibleElement();
        WebDriver driver = (WebDriver) Proxy.newProxyInstance(
                WebDriver.class.getClassLoader(),
                new Class<?>[] {WebDriver.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "findElement" -> {
                        if (footerLookups.incrementAndGet() == 1) {
                            throw new NoSuchElementException("Footer has not rendered yet");
                        }
                        yield visibleElement;
                    }
                    case "toString" -> "delayed-footer-driver";
                    default -> throw new UnsupportedOperationException(method.getName());
                });

        new BrowserWaits(driver).waitForPageFactoryElements(visibleElement);

        assertEquals(footerLookups.get(), 2, "The wait should retry the delayed footer lookup");
    }

    private static WebElement visibleElement() {
        return (WebElement) Proxy.newProxyInstance(
                WebElement.class.getClassLoader(),
                new Class<?>[] {WebElement.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "isDisplayed" -> true;
                    case "toString" -> "visible-element";
                    default -> throw new UnsupportedOperationException(method.getName());
                });
    }
}
