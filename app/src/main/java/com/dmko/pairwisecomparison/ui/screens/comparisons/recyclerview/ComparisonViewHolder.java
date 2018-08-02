package com.dmko.pairwisecomparison.ui.screens.comparisons.recyclerview;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmko.pairwisecomparison.R;
import com.dmko.pairwisecomparison.data.entities.Comparison;
import com.dmko.pairwisecomparison.ui.screens.comparisons.ComparisonsContract;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ComparisonViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.text_name) TextView textName;
    @BindView(R.id.image_edit) ImageView imageEdit;
    @BindView(R.id.image_delete) ImageView imageDelete;

    private Comparison comparison;

    public ComparisonViewHolder(View itemView, final ComparisonsContract.Presenter presenter) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        itemView.setOnClickListener(v -> {
            presenter.openComparisonSelected(comparison);
        });

        imageEdit.setOnClickListener(v -> {
            presenter.updateComparisonSelected(comparison);
        });

        imageDelete.setOnClickListener(v -> {
            presenter.deleteComparisonSelected(comparison);
        });
    }

    public void bindComparison(Comparison comparison) {
        this.comparison = comparison;
        textName.setText(comparison.getName());
    }
}
