package com.dmko.pairwisecomparison.ui.screens.comparison.options.recyclerview;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmko.pairwisecomparison.R;
import com.dmko.pairwisecomparison.data.entities.Option;
import com.dmko.pairwisecomparison.ui.screens.comparison.options.OptionsContract;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OptionViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.text_name) TextView textName;
    @BindView(R.id.image_delete) ImageView imageDelete;

    private Option option;


    public OptionViewHolder(View itemView, OptionsContract.Presenter presenter) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        itemView.setOnClickListener(v -> {
            presenter.updateOptionSelected(option);
        });

        imageDelete.setOnClickListener(v -> {
            presenter.deleteOptionSelected(option);
        });
    }

    public void bindOption(Option option) {
        this.option = option;
        textName.setText(option.getName());
    }
}
