package io.d2a.fuzzy.screens.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.navigation.NavigationDirection;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;

public class ResultListWidget extends AlwaysSelectedEntryListWidget<ResultEntry> {

    public ResultListWidget(final MinecraftClient minecraftClient,
                            final int width,
                            final int height,
                            final int top,
                            final int bottom) {
        super(minecraftClient, width, height, top, bottom, 15);
    }

    public void selectDirection(final NavigationDirection direction) {
        final ResultEntry entry = this.getNeighboringEntry(direction);
        System.out.println("Entry in pos: " + direction + " = " + entry);
        if (entry != null) {
            this.setSelected(entry);
        }
    }

    public void execute() {

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
