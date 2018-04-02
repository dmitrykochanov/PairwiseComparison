package com.dmko.pairwisecomparison.ui.screens.comparison.optioncomparisons.filters;

import com.dmko.pairwisecomparison.data.entities.OptionComparisonEntry;

public class NotComparedFilter implements OptionComparisonEntryFilter {
    @Override
    public boolean filter(OptionComparisonEntry entry) {
        return entry.getProgress() == 0;
    }

    @Override
    public String toString() {
        return "Not compared";
    }
}
