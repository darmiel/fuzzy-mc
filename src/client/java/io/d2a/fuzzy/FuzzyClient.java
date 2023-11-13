package io.d2a.fuzzy;

import io.d2a.fuzzy.config.ClothFuzzyConfig;
import io.d2a.fuzzy.config.DefaultFuzzyConfig;
import io.d2a.fuzzy.config.FuzzyConfig;
import io.d2a.fuzzy.screens.FuzzyCommandScreen;
import io.d2a.fuzzy.util.Command;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.*;

public class FuzzyClient implements ClientModInitializer {

    public static final Set<Command> SENT_COMMANDS = new LinkedHashSet<>();

    // Config
    private static final FuzzyConfig config;

    static {
        if (FabricLoader.getInstance().isModLoaded("cloth-config")) {
            config = AutoConfig.register(ClothFuzzyConfig.class, GsonConfigSerializer::new).getConfig();
        } else {
            config = new DefaultFuzzyConfig();
        }
    }

    // Keybinds
    private static KeyBinding openFuzzyCommandKeyBinding;

    @Override
    public void onInitializeClient() {
        openFuzzyCommandKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.fuzzy.open",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_J,
                "category.fuzzy.menu"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openFuzzyCommandKeyBinding.wasPressed()) {
                if (client.player != null) {
                    client.setScreen(new FuzzyCommandScreen(client.currentScreen));
                }
            }
        });

        ClientSendMessageEvents.COMMAND.register(command -> FuzzyClient.addCommand(Command.Type.CHAT, command));

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (FuzzyClient.getConfig().clearOnJoin()) {
                FuzzyClient.SENT_COMMANDS.clear();
            }
        });
    }

    public static FuzzyConfig getConfig() {
        return config;
    }

    public static void addCommand(final Command.Type type, final String commandText) {
        final Command command = new Command(type, commandText);
        // remove old command to move it to the top
        FuzzyClient.SENT_COMMANDS.remove(command);
        FuzzyClient.SENT_COMMANDS.add(command);
    }

}