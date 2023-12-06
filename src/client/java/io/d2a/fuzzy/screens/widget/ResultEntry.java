package io.d2a.fuzzy.screens.widget;

import io.d2a.fuzzy.FuzzyClient;
import io.d2a.fuzzy.util.Command;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.text.Text;

import java.awt.*;

public class ResultEntry extends AlwaysSelectedEntryListWidget.Entry<ResultEntry> {

    private final TextRenderer textRenderer;
    private final Command command;
    private final int score;

    public ResultEntry(final TextRenderer textRenderer, final Command command, final int score) {
        this.textRenderer = textRenderer;
        this.command = command;
        this.score = score;
    }

    @Override
    public String toString() {
        return this.command.command();
    }

    @Override
    public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        final String commandPrefix = this.command.type().getPrefix();
        final int commandPrefixWidth = this.textRenderer.getWidth(commandPrefix);
        context.drawText(this.textRenderer, commandPrefix, x, y + 1, Color.GRAY.getRGB(), false);

        final String score = String.valueOf(this.score);
        final int scoreWidth = this.textRenderer.getWidth(score);
        final Color scoreColor;
        if (this.score >= 90) {
            scoreColor = Color.MAGENTA;
        } else if (this.score >= 70) {
            scoreColor = Color.YELLOW;
        } else {
            scoreColor = Color.GRAY;
        }

        // build command preview
        final int maxCommandPreviewLength = entryWidth - 1 - commandPrefixWidth - 1 - scoreWidth;
        boolean truncated = false;
        String commandPreview = this.command.command();
        while (this.textRenderer.getWidth(commandPreview) > maxCommandPreviewLength) {
            commandPreview = commandPreview.substring(0, commandPreview.length() - 1);
            truncated = true;
        }
        if (truncated) {
            commandPreview = commandPreview.substring(0, commandPreview.length() - 3) + "...";
        }
        context.drawText(
                this.textRenderer,
                commandPreview,
                x + commandPrefixWidth,
                y + 1,
                this.command.type().getRgb(),
                true
        );

        if (FuzzyClient.getConfig().showScore() && this.score > 10) {
            context.drawText(
                    this.textRenderer,
                    score,
                    x + entryWidth - scoreWidth - 4,
                    y + 1,
                    scoreColor.getRGB(),
                    true
            );
        }
    }

    @Override
    public Text getNarration() {
        return Text.of(this.command.command());
    }

    public Command getCommand() {
        return command;
    }
}
