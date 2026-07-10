package theinternetwebsite.ui.driver;

import java.util.Locale;
import org.jetbrains.annotations.NotNull;

public record TestRunConfig(
        @NotNull String browser,
        @NotNull String browserVersion,
        boolean headless,
        @NotNull String baseUrl,
        @NotNull String seleniumGridBaseUrl,
        @NotNull String seleniumGridUrl,
        boolean useSeleniumGrid) {

    public static @NotNull TestRunConfig from(
            @NotNull String browser,
            @NotNull String browserVersion,
            @NotNull String headless,
            @NotNull String baseUrl,
            @NotNull String seleniumGridBaseUrl,
            @NotNull String seleniumGridUrl,
            @NotNull String useSeleniumGrid) {
        return new TestRunConfig(
                browser.toLowerCase(Locale.ROOT),
                browserVersion,
                Boolean.parseBoolean(headless),
                baseUrl,
                seleniumGridBaseUrl,
                seleniumGridUrl,
                Boolean.parseBoolean(useSeleniumGrid));
    }

    public @NotNull String activeBaseUrl() {
        return useSeleniumGrid ? seleniumGridBaseUrl : baseUrl;
    }

    public boolean usesRemoteDriver() {
        return useSeleniumGrid || browser.equals("remote-chrome");
    }
}
