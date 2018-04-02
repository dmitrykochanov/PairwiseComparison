package com.dmko.pairwisecomparison.ui.screens.comparison.optioncomparisons.filters;

import com.dmko.pairwisecomparison.data.entities.OptionComparisonEntry;

public interface OptionComparisonEntryFilter {

    boolean filter(OptionComparisonEntry entry);
}
