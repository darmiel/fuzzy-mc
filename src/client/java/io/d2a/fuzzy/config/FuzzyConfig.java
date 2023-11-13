package io.d2a.fuzzy.config;

public interface FuzzyConfig {

    int fuzzySearchCutoff();
    int fuzzySearchLimit();
    boolean clearOnJoin();
    boolean showScore();

}