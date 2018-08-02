package com.dmko.pairwisecomparison.ui.screens.comparison.optioncomparisons.spinnerfilter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import com.dmko.pairwisecomparison.R;
import com.dmko.pairwisecomparison.ui.screens.comparison.optioncomparisons.filters.OptionComparisonEntryFilter;

import java.util.List;

public class FilterTypesAdapter extends ArrayAdapter<OptionComparisonEntryFilter> {

    private List<OptionComparisonEntryFilter> filters;

    public FilterTypesAdapter(@NonNull Context context, List<OptionComparisonEntryFilter> filters) {
        super(context, R.layout.item_filter_type, R.id.text_name, filters);
        this.filters = filters;
    }

    public void setFilterTypes(List<OptionComparisonEntryFilter> filters) {
        this.filters.clear();
        this.filters.addAll(filters);
        notifyDataSetChanged();
    }
}
