package com.dmko.pairwisecomparison.ui.screens.comparison.comparisonresult.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dmko.pairwisecomparison.R;
import com.dmko.pairwisecomparison.data.entities.Option;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ComparisonResultAdapter extends RecyclerView.Adapter<ComparisonResultViewHolder> {
    private List<Map.Entry<Option, Integer>> results;

    public ComparisonResultAdapter() {
        this.results = Collections.emptyList();
    }

    public void setResults(List<Map.Entry<Option, Integer>> results) {
        this.results = results;
        notifyDataSetChanged();
    }

    public List<Map.Entry<Option, Integer>> getResults() {
        return results;
    }

    @NonNull
    @Override
    public ComparisonResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comparison_result, parent, false);
        return new ComparisonResultViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ComparisonResultViewHolder holder, int position) {
        holder.bindComparisonResult(results.get(position));
    }

    @Override
    public int getItemCount() {
        return results.size();
    }
}
