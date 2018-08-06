package com.dmko.pairwisecomparison.data.repositories;

public interface SettingsRepository {

    String getPromptText();

    boolean showPercentage();

    int getDefaultChart();

    int getChartColors();
}
