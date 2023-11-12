package io.d2a.fuzzy;

import io.d2a.fuzzy.screens.FuzzyCommandScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class FuzzyClient implements ClientModInitializer {

    public static final List<String> SENT_COMMANDS = new ArrayList<>();

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
    }

}