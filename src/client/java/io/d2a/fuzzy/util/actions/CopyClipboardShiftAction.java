package io.d2a.fuzzy.util.actions;

import io.d2a.fuzzy.screens.FuzzyCommandScreen;
import io.d2a.fuzzy.screens.widget.ResultEntry;
import io.d2a.fuzzy.screens.widget.SearchTextFieldWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.awt.*;

public class CopyClipboardShiftAction implements ShiftAction {

    @Override
    public char key() {
        return 'C';
    }

    @Override
    public boolean run(final ResultEntry entry, final FuzzyCommandScreen screen, final SearchTextFieldWidget widget) {
        final MinecraftClient client = MinecraftClient.getInstance();

        client.keyboard.setClipboard(entry.getCommand().getType().transform(entry.getCommand().getCommand()));
        if (client.player != null) {
            client.player.sendMessage(Text.literal("[Fuzzy] ")
                    .styled(style -> style.withColor(Color.GRAY.getRGB()))
                    .append(Text.translatable("text.fuzzy.messages.command-copied")
                            .styled(style -> style.withColor(Color.WHITE.getRGB())))
                    .append(Text.literal(entry.toString())
                            .styled(style -> style.withColor(Color.YELLOW.getRGB()).withItalic(true)))
            );
        }
        return false;
    }

}
