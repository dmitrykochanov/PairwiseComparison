package com.dmko.pairwisecomparison.ui.screens.comparison.comparisonresult.spinner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmko.pairwisecomparison.R;
import com.dmko.pairwisecomparison.ui.screens.comparison.comparisonresult.ComparisonResultContract;

import java.util.List;

public class ChartTypesAdapter extends ArrayAdapter<String> {
    private LayoutInflater inflater;
    private int resource;
    private List<String> items;
    private ComparisonResultContract.Presenter presenter;


    public ChartTypesAdapter(@NonNull Context context, int resource, List<String> items, ComparisonResultContract.Presenter presenter) {
        super(context, resource, items);
        inflater = LayoutInflater.from(context);
        this.items = items;
        this.resource = resource;
        this.presenter = presenter;
    }


    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent, false);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent, true);
    }

    private View createItemView(int position, @Nullable View convertView, @NonNull ViewGroup parent, boolean isSaveEnabled) {
        View view = inflater.inflate(resource, parent, false);

        TextView textName = view.findViewById(R.id.text_name);
        ImageView imageSave = view.findViewById(R.id.image_save);
        imageSave.setVisibility(isSaveEnabled && position != 2 ? View.VISIBLE : View.GONE);

        textName.setText(items.get(position));
        imageSave.setOnClickListener(v -> {
            presenter.saveChartSelected(position);
        });

        return view;
    }
}
