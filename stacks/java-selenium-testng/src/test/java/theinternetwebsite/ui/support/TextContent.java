package theinternetwebsite.ui.support;

import org.jetbrains.annotations.NotNull;

public final class TextContent {
    private TextContent() {}

    public static @NotNull String clean(@NotNull String text) {
        text = text.replaceAll("[^\\x00-\\x7F]", "");
        text = text.replaceAll("[\\p{Cntrl}&&[^\\r\\n\\t]]", "");
        text = text.replaceAll("\\p{C}", "");
        return text.trim();
    }
}
