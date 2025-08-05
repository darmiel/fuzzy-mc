package io.d2a.fuzzy.screens.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.navigation.NavigationDirection;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

public class ResultListWidget extends AlwaysSelectedEntryListWidget<ResultEntry> {

    public ResultListWidget(final MinecraftClient minecraftClient,
                            final int width,
                            final int height,
                            final int y) {
        super(minecraftClient, width, height, y, 15);
    }

    public void selectNextEntryInDirection(final NavigationDirection direction) {
        final ResultEntry entry = this.getNeighboringEntry(direction);
        if (entry != null) {
            this.setSelected(entry);
        }
    }

    @Override
    public void renderWidget(final DrawContext context, final int mouseX, final int mouseY, final float delta) {
        // display "no previous commands found"
        if (this.children().isEmpty()) {
            final Text text = Text.translatable("text.fuzzy.no-commands-found");
            context.drawText(
                    this.client.textRenderer,
                    text,
                    this.getX() + this.width / 2 - this.client.textRenderer.getWidth(text) / 2,
                    this.getY() + this.height / 2 - 5,
                    Color.PINK.getRGB(),
                    true
            );
            return;
        }
        super.renderWidget(context, mouseX, mouseY, delta);
    }

    public int getEntryY(final double mouseY) {
        return MathHelper.floor(mouseY - this.getY())
                - this.headerHeight + (int) this.getScrollAmount() - 2;
    }

    public int getEntryHeight() {
        return this.itemHeight;
    }

    public ResultEntry at(final int n) {
        if (n < 0 || n >= this.children().size()) {
            return null;
        }
        return this.children().get(n);
    }

    public boolean isMouseOver(final double mouseX, final double mouseY) {
        return mouseX >= this.getX()
                && mouseX <= this.getX() + this.width
                && mouseY >= this.getY()
                && mouseY <= this.getY() + this.height;
    }

    @Override
    public int getRowWidth() {
        return this.width;
    }

    @Override
    protected int getScrollbarX() {
        return -10;
    }

    @Override
    public void setFocused(boolean focused) {
        // never ever focus that thing
        super.setFocused(false);
    }

}
