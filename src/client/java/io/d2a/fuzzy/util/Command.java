package io.d2a.fuzzy.util;

import java.awt.*;
import java.util.Objects;
import java.util.function.Function;

public record Command(Type type, String command) {

    public enum Type {
        COMMAND_BLOCK(
                "@ ",
                Color.YELLOW.getRGB(),
                command -> command
        ),
        CHAT(
                "/ ",
                Color.WHITE.getRGB(),
                command -> "/" + command
        ),
        HISTORY(
                "* ",
                Color.GRAY.getRGB(),
                command -> "/" + command
        );

        private final int rgb;
        private final String prefix;
        private final Function<String, String> transformer;

        Type(final String prefix,
             final int rgb,
             final Function<String, String> transformer) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Command command1 = (Command) o;
        return command.equals(command1.command);
    }

    @Override
    public int hashCode() {
        return Objects.hash(command);
    }

}
