package io.d2a.fuzzy.config;

public class DefaultFuzzyConfig implements FuzzyConfig {

    @Override
    public int fuzzySearchCutoff() {
        return 0;
    }

    @Override
    public int fuzzySearchLimit() {
        return 100;
    }

    @Override
    public boolean clearOnJoin() {
        return false;
    }

    @Override
    public boolean showScore() {
        return true;
    }

    @Override
    public boolean enableShiftActions() {
        return true;
    }

    @Override
    public boolean enableCommandBlockSync() {
        return true;
    }

}
