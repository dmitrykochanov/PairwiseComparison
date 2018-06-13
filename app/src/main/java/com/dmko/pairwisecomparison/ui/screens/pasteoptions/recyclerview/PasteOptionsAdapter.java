package com.dmko.pairwisecomparison.ui.screens.pasteoptions.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dmko.pairwisecomparison.R;
import com.dmko.pairwisecomparison.data.entities.Option;
import com.dmko.pairwisecomparison.ui.screens.pasteoptions.PasteOptionsContract;
import com.dmko.pairwisecomparison.ui.screens.pasteoptions.PasteOptionsPresenter;

import java.util.Collections;
import java.util.List;

public class PasteOptionsAdapter extends RecyclerView.Adapter<PasteOptionsViewHolder> {

    private List<Option> options;
    private PasteOptionsContract.Presenter presenter;

    public PasteOptionsAdapter(PasteOptionsContract.Presenter presenter) {
        this.options = Collections.emptyList();
        this.presenter = presenter;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PasteOptionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_pasted_option, parent, false);
        return new PasteOptionsViewHolder(itemView, presenter);
    }

    @Override
    public void onBindViewHolder(@NonNull PasteOptionsViewHolder holder, int position) {
        Option option = options.get(position);
        holder.bindOption(option);
    }

    @Override
    public int getItemCount() {
        return options.size();
    }
}
