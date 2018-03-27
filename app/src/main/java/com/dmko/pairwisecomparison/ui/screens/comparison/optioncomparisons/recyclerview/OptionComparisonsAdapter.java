package com.dmko.pairwisecomparison.ui.screens.comparison.optioncomparisons.recyclerview;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dmko.pairwisecomparison.R;
import com.dmko.pairwisecomparison.data.entities.OptionComparisonEntry;
import com.dmko.pairwisecomparison.ui.screens.comparison.optioncomparisons.OptionComparisonsContract;

import java.util.Collections;
import java.util.List;

import static com.dmko.pairwisecomparison.utils.LogTags.LOG_APP;

public class OptionComparisonsAdapter extends RecyclerView.Adapter<OptionComparisonViewHolder> {
    private OptionComparisonsContract.Presenter presenter;
    private List<OptionComparisonEntry> optionComparisons;

    public OptionComparisonsAdapter(OptionComparisonsContract.Presenter presenter) {
        this.presenter = presenter;
        optionComparisons = Collections.emptyList();
    }

    public void setOptionComparisons(List<OptionComparisonEntry> optionComparisons) {
        this.optionComparisons = optionComparisons;
        notifyDataSetChanged();
    }

    public List<OptionComparisonEntry> getOptionComparisons() {
        return optionComparisons;
    }

    @NonNull
    @Override
    public OptionComparisonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_option_comparison, parent, false);
        return new OptionComparisonViewHolder(itemView, presenter);
    }

    @Override
    public void onBindViewHolder(@NonNull OptionComparisonViewHolder holder, int position) {
        OptionComparisonEntry optionComparison = optionComparisons.get(position);
        Log.i(LOG_APP, "Binding " + optionComparison + " to the " + OptionComparisonViewHolder.class.getSimpleName());
        holder.bindOptionComparison(optionComparison);
    }

    @Override
    public int getItemCount() {
        return optionComparisons.size();
    }
}
