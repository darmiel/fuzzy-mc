package io.d2a.fuzzy.screens;

import io.d2a.fuzzy.FuzzyClient;
import io.d2a.fuzzy.screens.widget.ResultEntry;
import io.d2a.fuzzy.screens.widget.ResultListWidget;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.navigation.GuiNavigation;
import net.minecraft.client.gui.navigation.GuiNavigationPath;
import net.minecraft.client.gui.navigation.NavigationDirection;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

@Environment(EnvType.CLIENT)
public class FuzzyCommandScreen extends Screen {

    private final Screen parent;

    public FuzzyCommandScreen(final Screen parent) {
        super(Text.literal("Fuzzy Command Search"));
        this.parent = parent;
    }

    private String previousSearch = null;

    private TextFieldWidget searchFieldWidget;
    private ResultListWidget listWidget;

    final int padding = 10;
    final int searchFieldHeight = 16;

    @Override
    protected void init() {
        super.init();

        final int resultBoxWidth = Math.min(super.width / 2, 500);
        final int resultBoxHeight = Math.min(super.height / 2, 300);

        final int resultBoxX = super.width / 2 - resultBoxWidth / 2;
        final int resultBoxY = super.height / 2 - resultBoxHeight / 2 - searchFieldHeight - padding;

        final int searchFieldX = super.width / 2 - resultBoxWidth / 2;
        final int searchFieldY = resultBoxY + resultBoxHeight + padding;

        // search field widget
        this.searchFieldWidget = new TextFieldWidget(
                super.textRenderer,
                searchFieldX,
                searchFieldY,
                resultBoxWidth,
                searchFieldHeight,
                Text.of("Search...")
        ) {
            @Override
            public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
                // select command search result above
                if (keyCode == GLFW.GLFW_KEY_UP) {
                    listWidget.selectDirection(NavigationDirection.UP);
                    return true;
                }
                // select command search result below
                if (keyCode == GLFW.GLFW_KEY_DOWN) {
                    listWidget.selectDirection(NavigationDirection.DOWN);
                    return true;
                }
                // execute command
                if (keyCode == GLFW.GLFW_KEY_ENTER) {
                    execute();
                    return true;
                }
                return super.keyPressed(keyCode, scanCode, modifiers);
            }
        };
        this.searchFieldWidget.setChangedListener(text -> this.search(text, false));
        this.focusOn(this.searchFieldWidget);
        super.addDrawableChild(this.searchFieldWidget);

        // command list widget
        listWidget = new ResultListWidget(
                super.client,
                resultBoxWidth,
                resultBoxHeight,
                resultBoxY,
                resultBoxY + resultBoxHeight
        );
        listWidget.setRenderBackground(false);
        listWidget.setLeftPos(resultBoxX);
        super.addDrawableChild(listWidget);

        // call search to initially fill all commands
        this.search("", true);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        final int resultBoxWidth = Math.min(super.width / 2, 500);
        final int resultBoxHeight = Math.min(super.height / 2, 300);

        final int resultBoxX = super.width / 2 - resultBoxWidth / 2;
        final int resultBoxY = super.height / 2 - resultBoxHeight / 2 - searchFieldHeight - padding;

        final int searchFieldY = resultBoxY + resultBoxHeight + padding;

        // draw background
        context.fill(
                resultBoxX - padding,
                resultBoxY - padding,
                resultBoxX + resultBoxWidth + padding,
                searchFieldY + searchFieldHeight + padding,
                Color.BLACK.getRGB()
        );

        super.render(context, mouseX, mouseY, delta);
    }

    private void search(final String text, final boolean force) {
        if (this.listWidget == null) {
            return;
        }

        // only search if query changes
        if (!force && this.previousSearch != null && this.previousSearch.equals(text)) {
            return;
        }
        this.previousSearch = text;

        // clear any remaining children
        this.listWidget.children().clear();

        // fuzzy search in commands
        if (text.length() > 0) {
            FuzzySearch.extractTop(text, FuzzyClient.SENT_COMMANDS, 100)
                    .forEach(command ->
                            listWidget.children().add(new ResultEntry(
                                    super.textRenderer,
                                    command.getString(),
                                    command.getScore()
                            ))
                    );
        } else {
            FuzzyClient.SENT_COMMANDS
                    .forEach(command ->
                            listWidget.children().add(0, new ResultEntry(
                                    super.textRenderer,
                                    command,
                                    -1
                            ))
                    );
        }

        // select first children
        if (listWidget.children().size() > 0) {
            listWidget.setSelected(listWidget.children().get(0));
        } else {
            listWidget.setSelected(null);
        }

        // reset scroll position
        listWidget.setScrollAmount(0);
    }

    public void execute() {
        //noinspection DataFlowIssue
        super.client.setScreen(this.parent);

        final ResultEntry entry = this.listWidget.getSelectedOrNull();
        if (entry == null) {
            return;
        }
        if (super.client == null || super.client.player == null) {
            return;
        }
        super.client.player.networkHandler.sendChatCommand(entry.toString().substring(1));
    }

    @Nullable
    @Override
    public GuiNavigationPath getNavigationPath(GuiNavigation navigation) {
        return null;
    }

    @Override
    public void setFocused(@Nullable Element focused) {
        // make sure we can only select the search field
        if (focused == null || !focused.equals(this.searchFieldWidget)) {
            return;
        }
        super.setFocused(focused);
    }
}
