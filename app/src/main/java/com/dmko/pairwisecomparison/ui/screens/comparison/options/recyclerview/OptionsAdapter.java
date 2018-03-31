package com.dmko.pairwisecomparison.ui.screens.comparison.options.recyclerview;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dmko.pairwisecomparison.R;
import com.dmko.pairwisecomparison.data.entities.Option;
import com.dmko.pairwisecomparison.ui.screens.comparison.options.OptionsContract;

import java.util.Collections;
import java.util.List;

public class OptionsAdapter extends RecyclerView.Adapter<OptionViewHolder> {
    private OptionsContract.Presenter presenter;
    private List<Option> options;

    public OptionsAdapter(OptionsContract.Presenter presenter) {
        options = Collections.emptyList();
        this.presenter = presenter;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_option, parent, false);
        return new OptionViewHolder(itemView, presenter);
    }

    @Override
    public void onBindViewHolder(@NonNull OptionViewHolder holder, int position) {
        Option option = options.get(position);
        holder.bindOption(option);
    }

    @Override
    public int getItemCount() {
        return options.size();
    }
}
