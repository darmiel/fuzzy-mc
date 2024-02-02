package io.d2a.fuzzy.screens;

import io.d2a.fuzzy.FuzzyClient;
import io.d2a.fuzzy.config.ClothFuzzyConfig;
import io.d2a.fuzzy.config.DefaultFuzzyConfig;
import io.d2a.fuzzy.screens.widget.ResultEntry;
import io.d2a.fuzzy.screens.widget.ResultListWidget;
import io.d2a.fuzzy.screens.widget.SearchTextFieldWidget;
import io.d2a.fuzzy.util.Command;
import io.d2a.fuzzy.util.actions.ShiftAction;
import io.d2a.fuzzy.util.text.ColoredText;
import io.d2a.fuzzy.util.text.TextMerge;
import me.shedaniel.autoconfig.AutoConfig;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.navigation.GuiNavigation;
import net.minecraft.client.gui.navigation.GuiNavigationPath;
import net.minecraft.client.gui.navigation.NavigationDirection;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

@Environment(EnvType.CLIENT)
public class FuzzyCommandScreen extends Screen {

    private final Screen parent;

    public FuzzyCommandScreen(final Screen parent) {
        super(
                Text.translatable("text.fuzzy.command-screen-title")
        );
        this.parent = parent;
    }

    private String previousSearch = null;

    private long lastClickTime;
    private Command lastClickCommand;


    private SearchTextFieldWidget searchFieldWidget;
    private ResultListWidget resultListWidget;


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
        this.searchFieldWidget = new SearchTextFieldWidget(
                this,
                super.textRenderer,
                searchFieldX,
                searchFieldY,
                resultBoxWidth,
                searchFieldHeight,
                Text.translatable("text.fuzzy.search")
        );
        this.searchFieldWidget.setChangedListener(text -> this.search(text, false));
        this.searchFieldWidget.setResultConsumer((result, entry) -> {
            switch (result) {
                case EXECUTE -> this.execute();
                case SUGGEST -> this.suggest();
            }
        });
        this.focusOn(this.searchFieldWidget);
        super.addDrawableChild(this.searchFieldWidget);

        // command list widget
        resultListWidget = new ResultListWidget(
                super.client,
                resultBoxWidth,
                resultBoxHeight,
                resultBoxY,
                resultBoxY + resultBoxHeight
        );
        resultListWidget.setRenderBackground(false);
        resultListWidget.setLeftPos(resultBoxX);
        super.addDrawableChild(resultListWidget);

        // call search to initially fill all commands
        this.updateResults();

        if (super.client == null) {
            return;
        }

        // open configuration button
        final List<ButtonWidget> buttons = new ArrayList<>();
        if (!(FuzzyClient.getConfig() instanceof DefaultFuzzyConfig)) {
            buttons.add(ButtonWidget.builder(
                            Text.translatable("text.fuzzy.button-config"), button -> {
                                final Screen configScreen = AutoConfig.getConfigScreen(
                                        ClothFuzzyConfig.class,
                                        super.client.currentScreen
                                ).get();
                                super.client.setScreen(configScreen);
                            })
                    .build());
        }

        // clear recent commands button
        buttons.add(ButtonWidget.builder(
                Text.translatable("text.fuzzy.button-clear"),
                button -> {
                    FuzzyClient.SENT_COMMANDS.clear();
                    FuzzyCommandScreen.this.updateResults();
                }
        ).build());

        // find button with max. length
        int buttonMaxWidth = 0;
        for (final ButtonWidget button : buttons) {
            final int buttonMessageLength = super.client.textRenderer.getWidth(button.getMessage());
            if (buttonMessageLength > buttonMaxWidth) {
                buttonMaxWidth = buttonMessageLength;
            }
        }

        // add button/s
        final int buttonPadding = 10;

        int lastButtonY = resultBoxY - padding;
        for (final ButtonWidget button : buttons) {
            button.setY(lastButtonY);
            button.setX(resultBoxX + resultBoxWidth + 2 * this.padding);
            button.setWidth(buttonMaxWidth + 10);

            lastButtonY += button.getHeight() + buttonPadding;
            this.addDrawableChild(button);
        }
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

        TextMerge.of(
                ColoredText.ofString("[", Color.GRAY),
                ColoredText.ofTranslatable("text.fuzzy.key.enter", Color.RED),
                ColoredText.ofString("]: ", Color.GRAY),
                ColoredText.ofTranslatable("text.fuzzy.action.execute", Color.LIGHT_GRAY),
                ColoredText.ofString(" | [", Color.GRAY),
                ColoredText.ofTranslatable("text.fuzzy.key.tab", Color.RED),
                ColoredText.ofString("]: ", Color.GRAY),
                ColoredText.ofTranslatable("text.fuzzy.action.suggest", Color.LIGHT_GRAY)
        ).drawCentered(
                super.textRenderer, context, super.width / 2, searchFieldY + searchFieldHeight + 2 * padding
        );

        if (FuzzyClient.getConfig().enableShiftActions() && this.searchFieldWidget.isShiftDown()) {
            // display shift actions
            int lastTextY = resultBoxY - padding;

            for (final ShiftAction action : ShiftAction.SHIFT_ACTIONS) {
                final Text keyText = Text.of("[" + action.key() + "]: ");
                context.drawText(
                        super.textRenderer,
                        keyText,
                        this.padding,
                        lastTextY,
                        Color.YELLOW.getRGB(),
                        true
                );

                final Text description = Text.translatable("text.fuzzy.action." + action.getClass().getSimpleName());
                context.drawText(
                        super.textRenderer,
                        description,
                        this.padding + super.textRenderer.getWidth(keyText),
                        lastTextY,
                        Color.WHITE.getRGB(),
                        true
                );

                lastTextY += super.textRenderer.fontHeight + 2;
            }
        }

        super.render(context, mouseX, mouseY, delta);
    }

    public void updateResults() {
        this.search(this.searchFieldWidget.getText(), true);
    }

    private void search(final String text, final boolean force) {
        if (this.resultListWidget == null) {
            return;
        }

        // only search if query changes
        if (!force && this.previousSearch != null && this.previousSearch.equals(text)) {
            return;
        }
        this.previousSearch = text;

        // clear any remaining children
        this.resultListWidget.children().clear();

        // fuzzy search in commands
        if (text.length() > 0) {
            FuzzySearch.extractTop(
                            text,
                            FuzzyClient.SENT_COMMANDS,
                            Command::command,
                            FuzzyClient.getConfig().fuzzySearchLimit(),
                            FuzzyClient.getConfig().fuzzySearchCutoff()
                    )
                    .forEach(command ->
                            resultListWidget.children().add(new ResultEntry(
                                    super.textRenderer,
                                    command.getReferent(),
                                    command.getScore()
                            ))
                    );
        } else {
            FuzzyClient.SENT_COMMANDS
                    .forEach(command ->
                            resultListWidget.children().add(0, new ResultEntry(
                                    super.textRenderer,
                                    command,
                                    -1
                            ))
                    );
        }

        // select first children
        if (resultListWidget.children().size() > 0) {
            resultListWidget.setSelected(resultListWidget.children().get(0));
        } else {
            resultListWidget.setSelected(null);
        }

        // reset scroll position
        resultListWidget.setScrollAmount(0);
    }

    private void check(final BiConsumer<MinecraftClient, ResultEntry> entryConsumer) {
        this.check(entryConsumer, true);
    }

    private void check(final BiConsumer<MinecraftClient, ResultEntry> entryConsumer, final boolean backToParent) {
        if (backToParent) {
            this.close();
        }
        final ResultEntry entry = this.resultListWidget.getSelectedOrNull();
        if (entry == null) {
            return;
        }
        if (super.client == null || super.client.player == null) {
            return;
        }
        entryConsumer.accept(super.client, entry);
    }

    public void execute() {
        this.check((client, entry) ->
                // we already checked if the player is null in the check function
                Objects.requireNonNull(client.player).networkHandler.
                        sendChatCommand(Command.Type.COMMAND_BLOCK.transform(entry.toString()))
        );
    }

    public void suggest() {
        this.check((client, entry) -> {
            final ChatScreen chatScreen = new ChatScreen(Command.Type.CHAT.transform(entry.toString()));
            client.setScreen(chatScreen);
        }, false);
    }

    private void selectEntry(final ResultEntry entry) {
        this.resultListWidget.setSelected(entry);
        if (!entry.getCommand().equals(this.lastClickCommand)) {
            this.lastClickCommand = null;
        }
    }

    private boolean onResultListClicked(final double mouseY, final int button) {
        // click selection
        final int entryY = this.resultListWidget.getEntryY(mouseY);
        if (entryY > 0) {
            final int entryIndex = entryY / this.resultListWidget.getEntryHeight();
            final ResultEntry entry = this.resultListWidget.at(entryIndex);
            if (entry != null) {
                this.selectEntry(entry);
            }
        }

        // double click
        final ResultEntry selectedEntry = this.resultListWidget.getSelectedOrNull();
        if (selectedEntry != null) {
            final long timeMs = Util.getMeasuringTimeMs();
            if (timeMs - this.lastClickTime <= 350
                    && this.lastClickCommand != null
                    && this.lastClickCommand.equals(selectedEntry.getCommand())) {
                if (button == 0) {
                    this.execute();
                } else if (button == 1) {
                    this.suggest();
                }
                return true;
            }

            this.lastClickTime = timeMs;
            this.lastClickCommand = selectedEntry.getCommand();
        }
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.resultListWidget.isMouseOver(mouseX, mouseY)
                && this.onResultListClicked(mouseY, button)) {
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (hasControlDown()) {
            if (verticalAmount > 0) {
                resultListWidget.selectNextEntryInDirection(NavigationDirection.UP);
            } else {
                resultListWidget.selectNextEntryInDirection(NavigationDirection.DOWN);
            }
            return true;
        }

        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
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

    public ResultListWidget getResultListWidget() {
        return resultListWidget;
    }

    public void close() {
        //noinspection DataFlowIssue
        super.client.setScreen(this.parent);
    }

}
