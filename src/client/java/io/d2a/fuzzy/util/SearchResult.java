package io.d2a.fuzzy.util;

import org.lwjgl.glfw.GLFW;

public enum SearchResult {
    EXECUTE(GLFW.GLFW_KEY_ENTER),
    SUGGEST(GLFW.GLFW_KEY_TAB);

    private final int keyCode;

    SearchResult(final int keyCode) {
        this.keyCode = keyCode;
    }

    public static SearchResult fromKeyCode(final int keyCode) {
        for (final SearchResult result : values()) {
            if (result.keyCode == keyCode) {
                return result;
            }
        }
        return null;
    }

}
