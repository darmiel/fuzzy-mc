package io.d2a.fuzzy.util.actions;

import io.d2a.fuzzy.screens.FuzzyCommandScreen;
import io.d2a.fuzzy.screens.widget.SearchTextFieldWidget;
import net.minecraft.client.gui.navigation.NavigationDirection;

public class PreviousEntryShiftAction implements ShiftAction {

    @Override
    public char key() {
        return 'K';
    }

    @Override
    public void run(final FuzzyCommandScreen screen, final SearchTextFieldWidget widget) {
        screen.getResultListWidget().selectNextEntryInDirection(NavigationDirection.UP);
    }

}
