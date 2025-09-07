package io.d2a.fuzzy.util.actions;

import io.d2a.fuzzy.FuzzyClient;
import io.d2a.fuzzy.screens.FuzzyCommandScreen;
import io.d2a.fuzzy.screens.widget.ResultEntry;
import io.d2a.fuzzy.screens.widget.SearchTextFieldWidget;
import io.d2a.fuzzy.util.Command;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;

import java.awt.*;

public class GiveCommandBlockShiftAction implements ShiftAction {

    @Override
    public char key() {
        return 'B';
    }

    @Override
    public boolean run(final ResultEntry entry, final FuzzyCommandScreen screen, final SearchTextFieldWidget widget) {
        final MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) {
            return true;
        }

        final ClientPlayNetworkHandler networkHandler = client.getNetworkHandler();
        if (networkHandler == null) {
            return true;
        }

        if (!client.player.isCreative()) {
            FuzzyClient.sendMessage(
                    client.player,
                    Text.translatable("text.fuzzy.error.not-creative")
                            .styled(style -> style.withColor(Color.RED.getRGB()))
            );
            return false;
        }

        // build command block item stack
        final NbtCompound compound = new NbtCompound();
        compound.putString("id", "minecraft:command_block");
        compound.putString("Command", Command.Type.COMMAND_BLOCK.transform(entry.getCommand().command()));

        final String command = "give @s minecraft:command_block[minecraft:block_entity_data=" + compound + "] 1";
        networkHandler.sendChatCommand(command);

        FuzzyClient.sendMessage(
                client.player,
                Text.translatable("text.fuzzy.messages.command-block-command-sent")
                        .styled(style -> style.withColor(Color.WHITE.getRGB()))
        );
        return false;
    }

}
