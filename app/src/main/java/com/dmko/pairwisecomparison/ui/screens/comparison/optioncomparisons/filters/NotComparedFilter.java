package com.dmko.pairwisecomparison.ui.screens.comparison.optioncomparisons.filters;

import android.content.res.Resources;

import com.dmko.pairwisecomparison.R;
import com.dmko.pairwisecomparison.data.entities.OptionComparisonEntry;

public class NotComparedFilter implements OptionComparisonEntryFilter {

    private String name;

    public NotComparedFilter(Resources resources) {
        name = resources.getString(R.string.filter_not_compared);
    }

    @Override
    public boolean filter(OptionComparisonEntry entry) {
        return entry.getProgress() == 0;
    }

    @Override
    public String toString() {
        return name;
    }
}
