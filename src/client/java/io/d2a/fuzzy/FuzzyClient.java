package io.d2a.fuzzy;

import io.d2a.fuzzy.config.ClothFuzzyConfig;
import io.d2a.fuzzy.config.DefaultFuzzyConfig;
import io.d2a.fuzzy.config.FuzzyConfig;
import io.d2a.fuzzy.screens.FuzzyCommandScreen;
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

import java.util.ArrayList;
import java.util.List;

public class FuzzyClient implements ClientModInitializer {

    public static final List<String> SENT_COMMANDS = new ArrayList<>();

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

        ClientSendMessageEvents.COMMAND.register(command -> {
            final String formattedCommand = "/" + command;

            // move command up
            SENT_COMMANDS.remove(formattedCommand);
            SENT_COMMANDS.add(formattedCommand);
        });

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (FuzzyClient.getConfig().clearOnJoin()) {
                FuzzyClient.SENT_COMMANDS.clear();
            }
        });
    }

    public static FuzzyConfig getConfig() {
        return config;
    }

}