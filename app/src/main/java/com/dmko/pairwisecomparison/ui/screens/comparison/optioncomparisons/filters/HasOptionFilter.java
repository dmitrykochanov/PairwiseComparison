package com.dmko.pairwisecomparison.ui.screens.comparison.optioncomparisons.filters;

import com.dmko.pairwisecomparison.data.entities.Option;
import com.dmko.pairwisecomparison.data.entities.OptionComparisonEntry;

public class HasOptionFilter implements OptionComparisonEntryFilter {
    private Option option;

    public HasOptionFilter(Option option) {
        this.option = option;
    }

    @Override
    public boolean filter(OptionComparisonEntry entry) {
        return entry.getFirstOption().equals(option) || entry.getSecondOption().equals(option);
    }

    @Override
    public String toString() {
        return option.getName();
    }
}
