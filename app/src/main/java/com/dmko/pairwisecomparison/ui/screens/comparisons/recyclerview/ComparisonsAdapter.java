package com.dmko.pairwisecomparison.ui.screens.comparisons.recyclerview;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dmko.pairwisecomparison.R;
import com.dmko.pairwisecomparison.data.entities.Comparison;
import com.dmko.pairwisecomparison.ui.screens.comparisons.ComparisonsContract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ComparisonsAdapter extends RecyclerView.Adapter<ComparisonViewHolder> {
    private ComparisonsContract.Presenter presenter;
    private List<Comparison> comparisons;


    public ComparisonsAdapter(ComparisonsContract.Presenter presenter) {
        comparisons = Collections.emptyList();
        this.presenter = presenter;
    }

    public void setComparisons(List<Comparison> comparisons) {
        this.comparisons = new ArrayList<>(comparisons);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ComparisonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_comparison, parent, false);
        return new ComparisonViewHolder(itemView, presenter);
    }

    @Override
    public void onBindViewHolder(@NonNull ComparisonViewHolder holder, int position) {
        Comparison comparison = comparisons.get(position);
        holder.bindComparison(comparison);
    }

    @Override
    public int getItemCount() {
        return comparisons.size();
    }
}
