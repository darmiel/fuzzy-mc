package io.d2a.fuzzy.mixin.client;

import io.d2a.fuzzy.FuzzyClient;
import io.d2a.fuzzy.util.Command;
import net.minecraft.client.gui.screen.ingame.AbstractCommandBlockScreen;
import net.minecraft.client.gui.screen.ingame.CommandBlockScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommandBlockScreen.class)
public abstract class CommandBlockSyncMixin {

    @Mixin(AbstractCommandBlockScreen.class)
    public interface AbstractCommandBlockScreenAccessor {
        @Accessor("consoleCommandTextField")
        TextFieldWidget getCommandTextField();
    }

    @Inject(method = "syncSettingsToServer", at = @At("RETURN"))
    private void syncSettingsToServer(final CallbackInfo ci) {
        if (!FuzzyClient.getConfig().enableCommandBlockSync()) {
            return;
        }
        FuzzyClient.addCommand(
                Command.Type.COMMAND_BLOCK,
                ((AbstractCommandBlockScreenAccessor) this).getCommandTextField().getText()
        );
    }

}
