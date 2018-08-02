package com.dmko.pairwisecomparison.ui.screens.comparison.optioncomparisons.filters;

import android.content.res.Resources;

import com.dmko.pairwisecomparison.R;
import com.dmko.pairwisecomparison.data.entities.OptionComparisonEntry;

public class AllFilter implements OptionComparisonEntryFilter {

    private String name;

    public AllFilter(Resources resources) {
        name = resources.getString(R.string.filter_all);
    }

    @Override
    public boolean filter(OptionComparisonEntry entry) {
        return true;
    }

    @Override
    public String toString() {
        return name;
    }
}
