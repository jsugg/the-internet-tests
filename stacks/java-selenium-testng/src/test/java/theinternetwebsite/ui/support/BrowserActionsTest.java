package theinternetwebsite.ui.support;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.expectThrows;

import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

public final class BrowserActionsTest {
    private static final String TARGET_URL = "http://localhost:7080/login";

    @Test(description = "Retries one timed-out idempotent navigation")
    public void navigateToRetriesOnce() {
        AtomicInteger attempts = new AtomicInteger();
        AtomicReference<String> currentUrl = new AtomicReference<>("about:blank");
        WebDriver driver = navigationDriver(attempts, currentUrl, 1);

        BrowserActions.navigateTo(driver, TARGET_URL);

        assertEquals(attempts.get(), 2);
        assertEquals(currentUrl.get(), TARGET_URL);
    }

    @Test(description = "Does not reload a target already reached before the timeout")
    public void navigateToAcceptsReachedTarget() {
        AtomicInteger attempts = new AtomicInteger();
        AtomicReference<String> currentUrl = new AtomicReference<>(TARGET_URL);
        WebDriver driver = navigationDriver(attempts, currentUrl, 1);

        BrowserActions.navigateTo(driver, TARGET_URL);

        assertEquals(attempts.get(), 1);
    }

    @Test(description = "Surfaces an actionable failure after the bounded retry")
    public void navigateToFailsAfterOneRetry() {
        AtomicInteger attempts = new AtomicInteger();
        AtomicReference<String> currentUrl = new AtomicReference<>("about:blank");
        WebDriver driver = navigationDriver(attempts, currentUrl, 2);

        TimeoutException failure = expectThrows(
                TimeoutException.class, () -> BrowserActions.navigateTo(driver, TARGET_URL));

        assertEquals(attempts.get(), 2);
        assertTrue(failure.getMessage().contains("timed out after 2 attempts"));
        assertEquals(failure.getSuppressed().length, 1);
    }

    private static WebDriver navigationDriver(
            AtomicInteger attempts, AtomicReference<String> currentUrl, int failures) {
        return (WebDriver) Proxy.newProxyInstance(
                WebDriver.class.getClassLoader(),
                new Class<?>[] {WebDriver.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "get" -> {
                        String requestedUrl = (String) args[0];
                        if (attempts.incrementAndGet() <= failures) {
                            throw new TimeoutException("simulated navigation timeout");
                        }
                        currentUrl.set(requestedUrl);
                        yield null;
                    }
                    case "getCurrentUrl" -> currentUrl.get();
                    case "toString" -> "navigation-driver";
                    default -> throw new UnsupportedOperationException(method.getName());
                });
    }
}
