package com.dmko.pairwisecomparison.data.repositories.impl;

import com.dmko.pairwisecomparison.data.repositories.SettingsRepository;
import com.dmko.pairwisecomparison.data.sharedoreferences.SharedPreferencesHelper;

public class SettingsRepositoryImpl implements SettingsRepository {

    private SharedPreferencesHelper prefs;

    public SettingsRepositoryImpl(SharedPreferencesHelper prefs) {
        this.prefs = prefs;
    }

    @Override
    public String getPromptText() {
        return prefs.getPromptText();
    }

    @Override
    public boolean showPercentage() {
        return prefs.showPercentage();
    }

    @Override
    public int getDefaultChart() {
        return prefs.getDefaultChart();
    }

    @Override
    public int getChartColors() {
        return prefs.getChartColors();
    }
}
