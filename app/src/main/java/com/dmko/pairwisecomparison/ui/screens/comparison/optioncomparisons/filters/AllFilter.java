package com.dmko.pairwisecomparison.ui.screens.comparison.optioncomparisons.filters;

import com.dmko.pairwisecomparison.data.entities.OptionComparisonEntry;

public class AllFilter implements OptionComparisonEntryFilter {
    @Override
    public boolean filter(OptionComparisonEntry entry) {
        return true;
    }

    @Override
    public String toString() {
        return "All";
    }
}
