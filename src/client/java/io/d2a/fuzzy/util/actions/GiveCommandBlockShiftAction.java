package io.d2a.fuzzy.util.actions;

import io.d2a.fuzzy.FuzzyClient;
import io.d2a.fuzzy.screens.FuzzyCommandScreen;
import io.d2a.fuzzy.screens.widget.ResultEntry;
import io.d2a.fuzzy.screens.widget.SearchTextFieldWidget;
import io.d2a.fuzzy.util.Command;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

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
                            .styled(style -> style.withColor(Formatting.RED))
            );
            return false;
        }

        // build command block item stack
        final ItemStack stack = new ItemStack(Blocks.COMMAND_BLOCK, 1);
        final NbtCompound compound = new NbtCompound();
        compound.putString("Command", Command.Type.COMMAND_BLOCK.transform(entry.getCommand().command()));
        NbtCompound blockEntityTag = new NbtCompound();
        blockEntityTag.put("BlockEntityTag", compound);
        stack.set(DataComponentTypes.BLOCK_ENTITY_DATA, NbtComponent.of(blockEntityTag));

        final int slot = client.player.getInventory().selectedSlot;
        networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(36 + slot, stack));

        FuzzyClient.sendMessage(
                client.player,
                Text.translatable("text.fuzzy.messages.command-block-given")
                        .styled(style -> style.withColor(Color.WHITE.getRGB()))
        );
        return false;
    }

}
