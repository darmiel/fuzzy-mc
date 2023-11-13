package io.d2a.fuzzy.util;

import java.awt.*;
import java.util.Objects;
import java.util.function.Function;

public class Command {

    public enum Type {
        COMMAND_BLOCK(
                "# ",
                Color.YELLOW.getRGB(),
                command -> command
        ),
        CHAT(
                "> ",
                Color.WHITE.getRGB(),
                command -> "/" + command
        );

        private final int rgb;
        private final String prefix;
        private final Function<String, String> transformer;

        Type(final String prefix, final int rgb, final Function<String, String> transformer) {
            this.prefix = prefix;
            this.rgb = rgb;
            this.transformer = transformer;
        }

        public String getPrefix() {
            return prefix;
        }

        public int getRgb() {
            return rgb;
        }

        public String transform(final String input) {
            return this.transformer.apply(input);
        }
    }

    private final Type type;
    private final String command;

    public Command(final Type type, final String command) {
        this.type = type;
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Command command1 = (Command) o;
        return type == command1.type && command.equals(command1.command);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, command);
    }

}
