package theinternetwebsite.ui.driver;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import org.openqa.selenium.edge.EdgeOptions;
import org.testng.annotations.Test;

public final class DriverFactoryTest {
    @Test(description = "Navigation returns after DOM readiness and has a bounded page-load timeout")
    public void navigationPolicyIsBoundedAndUsesEagerReadiness() {
        EdgeOptions options = new EdgeOptions();

        DriverFactory.configureNavigation(options);

        assertEquals(options.getCapability("pageLoadStrategy"), "eager");
        Object timeouts = options.getCapability("timeouts");
        assertTrue(timeouts instanceof Map<?, ?>, "Driver capabilities should contain a timeout map");
        assertEquals(((Map<?, ?>) timeouts).get("pageLoad"), DriverFactory.PAGE_LOAD_TIMEOUT.toMillis());
    }

    @Test(description = "Local Edge sessions suppress the fresh-profile first-run experience")
    public void localEdgeDisablesFirstRunExperience() {
        EdgeOptions options = new EdgeOptions();

        DriverFactory.configureLocalEdge(options);

        Object edgeOptions = options.getCapability(EdgeOptions.CAPABILITY);
        assertTrue(edgeOptions instanceof Map<?, ?>, "Edge capabilities should contain an options map");
        Object arguments = ((Map<?, ?>) edgeOptions).get("args");
        assertTrue(arguments instanceof List<?>, "Edge options should contain launch arguments");
        assertTrue(((List<?>) arguments).containsAll(List.of(
                        "--no-first-run", "--no-sandbox", "--disable-dev-shm-usage")),
                "Local Edge must skip first-run UI and retain Linux CI launch hardening");
    }
}
