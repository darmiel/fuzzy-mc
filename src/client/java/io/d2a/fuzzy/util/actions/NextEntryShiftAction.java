package io.d2a.fuzzy.util.actions;

import io.d2a.fuzzy.screens.FuzzyCommandScreen;
import io.d2a.fuzzy.screens.widget.ResultEntry;
import io.d2a.fuzzy.screens.widget.SearchTextFieldWidget;
import net.minecraft.client.gui.navigation.NavigationDirection;

public class NextEntryShiftAction implements ShiftAction {

    @Override
    public char key() {
        return 'J';
    }

    @Override
    public boolean run(final ResultEntry entry, final FuzzyCommandScreen screen, final SearchTextFieldWidget widget) {
        screen.getResultListWidget().selectNextEntryInDirection(NavigationDirection.DOWN);
        return true;
    }

}
