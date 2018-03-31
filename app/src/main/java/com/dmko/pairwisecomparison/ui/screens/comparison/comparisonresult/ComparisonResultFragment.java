package com.dmko.pairwisecomparison.ui.screens.comparison.comparisonresult;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dmko.pairwisecomparison.R;
import com.dmko.pairwisecomparison.data.entities.Option;
import com.dmko.pairwisecomparison.ui.base.mvp.impl.BaseFragment;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ComparisonResultFragment extends BaseFragment implements ComparisonResultContract.View {
    private static final String ARG_COMP_ID = "comp_id";

    @BindView(R.id.progress_loading) ProgressBar progressLoading;
    @BindView(R.id.chart_results) PieChart chartResults;
    @BindView(R.id.text_empty_title) TextView textEmptyTitle;
    @BindView(R.id.text_empty_description) TextView textEmptyDescription;

    @Inject ComparisonResultContract.Presenter presenter;

    private Unbinder unbinder;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getControllerComponent().inject(this);
        presenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_comparison_result, container, false);
        unbinder = ButterKnife.bind(this, view);

        chartResults.setVisibility(View.GONE);
        chartResults.setDrawHoleEnabled(false);
        chartResults.setUsePercentValues(true);
        chartResults.setEntryLabelColor(Color.BLACK);
        chartResults.setEntryLabelTextSize(14f);
        chartResults.setDrawEntryLabels(true);

        String comparisonId = null;
        if (getArguments() != null) {
            comparisonId = getArguments().getString(ARG_COMP_ID);
        }
        presenter.start(comparisonId);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void showLoading(boolean isLoading) {
        progressLoading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setResults(Map<Option, Integer> results) {
        if (results.size() == 0) {
            chartResults.setVisibility(View.GONE);
            textEmptyTitle.setVisibility(View.VISIBLE);
            textEmptyDescription.setVisibility(View.VISIBLE);
        } else {
            textEmptyTitle.setVisibility(View.GONE);
            textEmptyDescription.setVisibility(View.GONE);
            chartResults.setVisibility(View.VISIBLE);

            List<PieEntry> pieEntries = new ArrayList<>(results.size());
            for (Option option : results.keySet()) {
                pieEntries.add(new PieEntry(results.get(option), option.getName()));
            }
            Collections.sort(pieEntries, (p1, p2) -> Float.compare(p1.getValue(), p2.getValue()));

            PieDataSet dataSet = new PieDataSet(pieEntries, "");
            dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            dataSet.setSliceSpace(3f);
            dataSet.setSelectionShift(5f);


            PieData pieData = new PieData(dataSet);
            pieData.setValueFormatter(new PercentFormatter());
            pieData.setValueTextSize(14f);
            pieData.setDrawValues(true);
            chartResults.setData(pieData);
            chartResults.invalidate();
        }
    }

    public static ComparisonResultFragment newInstance(String comparisonId) {
        Bundle args = new Bundle();
        args.putString(ARG_COMP_ID, comparisonId);
        ComparisonResultFragment fragment = new ComparisonResultFragment();
        fragment.setArguments(args);
        return fragment;
    }
}