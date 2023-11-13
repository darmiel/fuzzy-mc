package io.d2a.fuzzy.util.actions;

import io.d2a.fuzzy.screens.FuzzyCommandScreen;
import io.d2a.fuzzy.screens.widget.SearchTextFieldWidget;

public interface ShiftAction {

    ShiftAction[] SHIFT_ACTIONS = new ShiftAction[]{
            new NextEntryShiftAction(),
            new PreviousEntryShiftAction(),
            new ClearEntriesShiftAction()
    };

    static ShiftAction fromKeyCode(final char keyCode) {
        for (final ShiftAction action : SHIFT_ACTIONS) {
            if (action.key() == keyCode) {
                return action;
            }
        }
        return null;
    }

    char key();

    void run(final FuzzyCommandScreen screen, final SearchTextFieldWidget widget);
}
