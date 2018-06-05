package com.dmko.pairwisecomparison.ui.screens.comparison.comparisonresult.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.dmko.pairwisecomparison.R;
import com.dmko.pairwisecomparison.data.entities.Option;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ComparisonResultViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.text_comparison_result) TextView textComparisonResult;
    @BindView(R.id.text_percentage) TextView textPercentage;

    public ComparisonResultViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindComparisonResult(Map.Entry<Option, Integer> comparisonResult) {
        textComparisonResult.setText(comparisonResult.getKey().getName());
        textPercentage.setText(String.format("%s%%", comparisonResult.getValue()));
    }
}
