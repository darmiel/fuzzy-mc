package io.d2a.fuzzy.util.text;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class TextMerge {

    private final List<Text> textList;

    private TextMerge(final List<Text> textList) {
        this.textList = textList;
    }

    public static TextMerge of(final Text...texts) {
        return new TextMerge(Arrays.asList(texts));
    }

    public void drawCentered(final TextRenderer textRenderer, final DrawContext context, final int x, final int y) {
        // get sum of widths
        int fullWidth = 0;
        for (final Text text : this.textList) {
            fullWidth += textRenderer.getWidth(text);
        }

        int lastX = x - fullWidth / 2;
        for (final Text text : this.textList) {
            final int color;
            if (text instanceof final ColoredText coloredText) {
                color = coloredText.getRgb();
            } else {
                color = Color.WHITE.getRGB();
            }
            context.drawText(
                    textRenderer,
                    text,
                    lastX,
                    y,
                    color,
                    true
            );
            lastX += textRenderer.getWidth(text);
        }
    }

}
