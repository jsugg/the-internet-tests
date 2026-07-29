package theinternetwebsite.ui.support;

import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BrowserActions {
    private static final Logger LOGGER = LoggerFactory.getLogger(BrowserActions.class);
    private static final int MAX_NAVIGATION_ATTEMPTS = 2;

    private BrowserActions() {}

    /** Opens a URL with one bounded retry for an idempotent timed-out navigation. */
    public static void navigateTo(@NotNull WebDriver driver, @NotNull String url) {
        TimeoutException firstFailure = null;
        for (int attempt = 1; attempt <= MAX_NAVIGATION_ATTEMPTS; attempt++) {
            try {
                driver.get(url);
                return;
            } catch (TimeoutException timeout) {
                if (hasReachedTarget(driver, url, timeout)) {
                    LOGGER.warn("Navigation timed out after reaching {}; continuing with page readiness checks", url);
                    return;
                }
                if (attempt == MAX_NAVIGATION_ATTEMPTS) {
                    TimeoutException failure = new TimeoutException(
                            "Navigation to " + url + " timed out after " + MAX_NAVIGATION_ATTEMPTS + " attempts",
                            timeout);
                    if (firstFailure != null) {
                        failure.addSuppressed(firstFailure);
                    }
                    throw failure;
                }
                firstFailure = timeout;
                LOGGER.warn("Navigation attempt {} of {} timed out for {}; retrying",
                        attempt, MAX_NAVIGATION_ATTEMPTS, url);
            }
        }
    }

    private static boolean hasReachedTarget(
            @NotNull WebDriver driver, @NotNull String url, @NotNull TimeoutException timeout) {
        try {
            return url.equals(driver.getCurrentUrl());
        } catch (WebDriverException currentUrlFailure) {
            timeout.addSuppressed(currentUrlFailure);
            return false;
        }
    }

    public static void reloadPage(@NotNull RemoteWebDriver driver) {
        driver.navigate().refresh();
    }

    public static void dragAndDropWithJavaScript(
            @NotNull RemoteWebDriver driver, @NotNull WebElement source, @NotNull WebElement destination) {
        JavascriptExecutor js = driver;
        js.executeScript("""
                function createEvent(typeOfEvent) {
                var event = document.createEvent('CustomEvent');
                event.initCustomEvent(typeOfEvent,true, true, null);
                event.dataTransfer = {
                data: {},
                setData: function (key, value) {
                this.data[key] = value;
                },
                getData: function (key) {
                return this.data[key];
                }
                };
                return event;
                }

                function dispatchEvent(element, event,transferData) {
                if (transferData !== undefined) {
                event.dataTransfer = transferData;
                }
                if (element.dispatchEvent) {
                element.dispatchEvent(event);
                } else if (element.fireEvent) {
                element.fireEvent("on" + event.type, event);
                }
                }

                function simulateHTML5DragAndDrop(element, destination) {
                var dragStartEvent =createEvent('dragstart');
                dispatchEvent(element, dragStartEvent);
                var dropEvent = createEvent('drop');
                dispatchEvent(destination, dropEvent,dragStartEvent.dataTransfer);
                var dragEndEvent = createEvent('dragend');
                dispatchEvent(element, dragEndEvent,dropEvent.dataTransfer);
                }

                var source = arguments[0];
                var destination = arguments[1];
                simulateHTML5DragAndDrop(source,destination);""", source, destination);
    }
}
