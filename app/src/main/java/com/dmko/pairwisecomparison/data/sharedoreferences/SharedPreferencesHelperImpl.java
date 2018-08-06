package com.dmko.pairwisecomparison.data.sharedoreferences;

import android.content.SharedPreferences;
import android.content.res.Resources;

import com.dmko.pairwisecomparison.R;

public class SharedPreferencesHelperImpl implements SharedPreferencesHelper {

    private String prompt_text;
    private String show_percentage;
    private String default_chart;
    private String chart_colors;

    private SharedPreferences prefs;

    public SharedPreferencesHelperImpl(Resources resources, SharedPreferences prefs) {
        this.prefs = prefs;
        prompt_text = resources.getString(R.string.key_prompt_text);
        show_percentage = resources.getString(R.string.key_show_percentage);
        default_chart = resources.getString(R.string.key_default_chart);
        chart_colors = resources.getString(R.string.key_chart_colors);
    }

    @Override
    public String getPromptText() {
        return prefs.getString(prompt_text, null);
    }

    @Override
    public boolean showPercentage() {
        return prefs.getBoolean(show_percentage, true);
    }

    @Override
    public int getDefaultChart() {
        String defaultChart = prefs.getString(default_chart, null);
        return Integer.parseInt(defaultChart);
    }

    @Override
    public int getChartColors() {
        String chartColors = prefs.getString(chart_colors, null);
        return Integer.parseInt(chartColors);
    }
}
