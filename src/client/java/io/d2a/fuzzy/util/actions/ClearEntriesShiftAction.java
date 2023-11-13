package io.d2a.fuzzy.util.actions;

import io.d2a.fuzzy.FuzzyClient;
import io.d2a.fuzzy.screens.FuzzyCommandScreen;
import io.d2a.fuzzy.screens.widget.ResultEntry;
import io.d2a.fuzzy.screens.widget.SearchTextFieldWidget;

public class ClearEntriesShiftAction implements ShiftAction {

    @Override
    public char key() {
        return 'X';
    }

    @Override
    public boolean run(final ResultEntry entry, final FuzzyCommandScreen screen, final SearchTextFieldWidget widget) {
        FuzzyClient.SENT_COMMANDS.remove(entry.getCommand());
        screen.updateResults();
        return true;
    }

}
