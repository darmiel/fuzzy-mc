package io.d2a.fuzzy.util.text;

import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;

import java.util.List;
import java.awt.*;

/**
 * Only used for {@link TextMerge}
 */
public class ColoredText implements Text {

    private final Text text;
    private final int rgb;

    private ColoredText(final Text text, final int rgb) {
        this.text = text;
        this.rgb = rgb;
    }

    public static ColoredText of(final Text text, final Color color) {
        return new ColoredText(text, color.getRGB());
    }

    public static ColoredText ofTranslatable(final String key, final Color color) {
        return new ColoredText(Text.translatable(key), color.getRGB());
    }

    public static ColoredText ofString(final String text, final Color color) {
        return new ColoredText(Text.of(text), color.getRGB());
    }

    public int getRgb() {
        return rgb;
    }

    ///

    @Override
    public Style getStyle() {
        return this.text.getStyle();
    }

    @Override
    public TextContent getContent() {
        return this.text.getContent();
    }

    @Override
    public List<Text> getSiblings() {
        return this.text.getSiblings();
    }

    @Override
    public OrderedText asOrderedText() {
        return this.text.asOrderedText();
    }

}
