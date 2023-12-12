package io.d2a.fuzzy.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "fuzzy")
public class ClothFuzzyConfig implements ConfigData, FuzzyConfig {

    @ConfigEntry.Gui.Excluded
    private static final DefaultFuzzyConfig defaultFuzzyConfig = new DefaultFuzzyConfig();

    ///

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    private int fuzzySearchCutoff = defaultFuzzyConfig.fuzzySearchCutoff();

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 1, max = 1000)
    private int fuzzySearchLimit = defaultFuzzyConfig.fuzzySearchLimit();

    @ConfigEntry.Gui.Tooltip
    private boolean clearOnJoin = defaultFuzzyConfig.clearOnJoin();

    @ConfigEntry.Gui.Tooltip
    private boolean showScore = defaultFuzzyConfig.showScore();

    @ConfigEntry.Gui.Tooltip
    private boolean enableShiftActions = defaultFuzzyConfig.enableShiftActions();

    @ConfigEntry.Gui.Tooltip
    private boolean enableCommandBlockSync = defaultFuzzyConfig.enableCommandBlockSync();

    @ConfigEntry.Gui.Tooltip
    private String ignoredCommandPrefixes = defaultFuzzyConfig.ignoredCommandPrefixes();

    @ConfigEntry.Gui.Tooltip
    private boolean loadCommandHistory = defaultFuzzyConfig.loadCommandHistory();

    @Override
    public int fuzzySearchCutoff() {
        return this.fuzzySearchCutoff;
    }

    @Override
    public int fuzzySearchLimit() {
        return this.fuzzySearchLimit;
    }

    @Override
    public boolean clearOnJoin() {
        return this.clearOnJoin;
    }

    @Override
    public boolean showScore() {
        return this.showScore;
    }

    @Override
    public boolean enableShiftActions() {
        return this.enableShiftActions;
    }

    @Override
    public boolean enableCommandBlockSync() {
        return this.enableCommandBlockSync;
    }

    @Override
    public String ignoredCommandPrefixes() {
        return this.ignoredCommandPrefixes;
    }

    @Override
    public boolean loadCommandHistory() {
        return this.loadCommandHistory;
    }

}
