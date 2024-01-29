package io.d2a.fuzzy.screens.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.navigation.NavigationDirection;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.awt.*;

public class ResultListWidget extends AlwaysSelectedEntryListWidget<ResultEntry> {

    public ResultListWidget(final MinecraftClient minecraftClient,
                            final int width,
                            final int height,
                            final int top,
                            final int bottom) {
        super(minecraftClient, width, height, top, bottom, 15);
    }

    public void selectNextEntryInDirection(final NavigationDirection direction) {
        final ResultEntry entry = this.getNeighboringEntry(direction);
        if (entry != null) {
            this.setSelected(entry);
        }
    }

    @Override
    public void render(final MatrixStack matrices, final int mouseX, final int mouseY, final float delta) {
        // display "no previous commands found"
        if (this.children().size() == 0) {
            final Text text = Text.translatable("text.fuzzy.no-commands-found");
            this.client.textRenderer.drawWithShadow(
                    matrices,
                    text,
                    this.left + this.width / 2 - this.client.textRenderer.getWidth(text) / 2,
                    this.top + this.height / 2 - 5,
                    Color.PINK.getRGB()
            );
            return;
        }
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public int getRowWidth() {
        return this.width;
    }

    @Override
    protected int getScrollbarPositionX() {
        return -10;
    }

    @Override
    public void setFocused(boolean focused) {
        // never ever focus that thing
        super.setFocused(false);
    }

}
