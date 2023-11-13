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
    public void run(final FuzzyCommandScreen screen, final SearchTextFieldWidget widget) {
        // get selected entry
        final ResultEntry entry = screen.getResultListWidget().getSelectedOrNull();
        if (entry != null) {
            FuzzyClient.SENT_COMMANDS.remove(entry.toString());
            screen.updateResults();
        }
    }

}
