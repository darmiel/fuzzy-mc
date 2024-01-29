package io.d2a.fuzzy.util;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.glfw.GLFW;

public enum SearchResult {
    EXECUTE(new int[]{GLFW.GLFW_KEY_ENTER, GLFW.GLFW_KEY_KP_ENTER}),
    SUGGEST(new int[]{GLFW.GLFW_KEY_TAB});

    private final int[] keyCodes;

    SearchResult(final int[] keyCodes) {
        this.keyCodes = keyCodes;
    }

    public static SearchResult fromKeyCode(final int keyCode) {
        for (final SearchResult result : values()) {
            if (ArrayUtils.contains(result.keyCodes, keyCode)) {
                return result;
            }
        }
        return null;
    }

}
