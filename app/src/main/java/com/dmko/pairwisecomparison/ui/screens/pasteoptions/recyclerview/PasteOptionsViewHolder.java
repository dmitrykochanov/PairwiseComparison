package com.dmko.pairwisecomparison.ui.screens.pasteoptions.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.dmko.pairwisecomparison.R;
import com.dmko.pairwisecomparison.data.entities.Option;
import com.dmko.pairwisecomparison.ui.screens.pasteoptions.PasteOptionsContract;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PasteOptionsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.input_option_name) EditText inputOptionName;
    @BindView(R.id.image_delete) ImageView imageDelete;
    private Option option;

    public PasteOptionsViewHolder(View itemView, PasteOptionsContract.Presenter presenter) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        imageDelete.setOnClickListener(v -> {
            presenter.deleteOption(option);
        });

        inputOptionName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                option.setName(s.toString());
                presenter.updateOption(option);
            }
        });
    }

    public void bindOption(Option option) {
        this.option = option;
        inputOptionName.setText(option.getName());
    }
}
