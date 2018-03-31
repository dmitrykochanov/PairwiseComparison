package com.dmko.pairwisecomparison.ui.screens.comparison.comparisonresult;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.dmko.pairwisecomparison.R;
import com.dmko.pairwisecomparison.data.entities.Option;
import com.dmko.pairwisecomparison.ui.base.mvp.impl.BaseFragment;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

public class ComparisonResultFragment extends BaseFragment implements ComparisonResultContract.View {
    private static final int[] MATERIAL_COLORS = {
            rgb("#4CAF50"), rgb("#FF5722"), rgb("#FFC107"),
            rgb("#F44336"), rgb("#2196F3"), rgb("#FF9800"),
            rgb("#009688"), rgb("#CDDC39"), rgb("#607D8B")
    };
    private static final String ARG_COMP_ID = "comp_id";
    private static final int PIE_CHART = 0;
    private static final int BAR_CHART = 1;

    @BindView(R.id.progress_loading) ProgressBar progressLoading;
    @BindView(R.id.chart_pie_results) PieChart pieChartResults;
    @BindView(R.id.chart_bar_results) HorizontalBarChart barChartResults;
    @BindView(R.id.text_empty_title) TextView textEmptyTitle;
    @BindView(R.id.text_empty_description) TextView textEmptyDescription;
    @BindView(R.id.spinner_chart_types) Spinner spinnerChartTypes;

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

        pieChartResults.setVisibility(View.GONE);
        pieChartResults.setDrawHoleEnabled(false);
        pieChartResults.setUsePercentValues(true);
        pieChartResults.setEntryLabelColor(Color.BLACK);
        pieChartResults.setEntryLabelTextSize(14f);
        pieChartResults.setDrawEntryLabels(true);
        pieChartResults.setDescription(null);
        pieChartResults.getLegend().setEnabled(false);


        barChartResults.setVisibility(View.GONE);
        barChartResults.setDescription(null);
        barChartResults.getLegend().setEnabled(false);
        barChartResults.getAxisLeft().setDrawGridLines(false);
        barChartResults.getAxisLeft().setDrawLabels(false);
        barChartResults.getAxisLeft().setDrawAxisLine(false);
        barChartResults.getAxisRight().setDrawGridLines(false);
        barChartResults.getAxisRight().setDrawLabels(false);
        barChartResults.getAxisRight().setDrawAxisLine(false);
        barChartResults.getXAxis().setDrawGridLines(false);
        barChartResults.getXAxis().setDrawLabels(false);
        barChartResults.getXAxis().setDrawAxisLine(false);
        barChartResults.getAxisLeft().setAxisMinimum(0);

        spinnerChartTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                switch (pos) {
                    case PIE_CHART:
                        presenter.setChartType(PIE_CHART);
                        break;
                    case BAR_CHART:
                        presenter.setChartType(BAR_CHART);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
    public void setResults(Map<Option, Integer> results, int chartType) {
        Timber.i("Setting results with chart type = %d", chartType);
        if (results.size() == 0) {
            pieChartResults.setVisibility(View.GONE);
            barChartResults.setVisibility(View.GONE);
            textEmptyTitle.setVisibility(View.VISIBLE);
            textEmptyDescription.setVisibility(View.VISIBLE);
            spinnerChartTypes.setVisibility(View.GONE);

        } else if (chartType == PIE_CHART) {
            textEmptyTitle.setVisibility(View.GONE);
            textEmptyDescription.setVisibility(View.GONE);
            spinnerChartTypes.setVisibility(View.VISIBLE);
            barChartResults.setVisibility(View.GONE);
            pieChartResults.setVisibility(View.VISIBLE);

            setupPieChart(results);

        } else if (chartType == BAR_CHART) {
            textEmptyTitle.setVisibility(View.GONE);
            textEmptyDescription.setVisibility(View.GONE);
            spinnerChartTypes.setVisibility(View.VISIBLE);
            pieChartResults.setVisibility(View.GONE);
            barChartResults.setVisibility(View.VISIBLE);

            setupBarChart(results);
        }
    }

    public static ComparisonResultFragment newInstance(String comparisonId) {
        Bundle args = new Bundle();
        args.putString(ARG_COMP_ID, comparisonId);
        ComparisonResultFragment fragment = new ComparisonResultFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void setupPieChart(Map<Option, Integer> results) {
        List<PieEntry> pieEntries = new ArrayList<>(results.size());
        for (Option option : results.keySet()) {
            pieEntries.add(new PieEntry(results.get(option), option.getName()));
        }
        Collections.sort(pieEntries, (p1, p2) -> Float.compare(p1.getValue(), p2.getValue()));

        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        dataSet.setColors(MATERIAL_COLORS);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);


        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(14f);
        pieData.setDrawValues(true);
        pieChartResults.setData(pieData);
        pieChartResults.animateY(500);
    }

    private void setupBarChart(Map<Option, Integer> results) {
        List<BarEntry> barEntries = new ArrayList<>(results.size());
        List<Map.Entry<Option, Integer>> entries = new ArrayList<>(results.entrySet());
        Collections.sort(entries, (e1, e2) -> e1.getValue().compareTo(e2.getValue()));
        float y = 0;
        for (Map.Entry<Option, Integer> mapEntry : entries) {
            //Creating fake 0.001 bar to draw title to the right of it so it looks like the text is inside the main bar
            BarEntry entry = new BarEntry(y, new float[]{0.001f, mapEntry.getValue()}, " " + mapEntry.getKey().getName() + "(" + mapEntry.getValue() + "%)");
            barEntries.add(entry);
            y += 1;
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "");
        barDataSet.setColors(MATERIAL_COLORS);
        barDataSet.setDrawValues(true);

        BarData barData = new BarData(barDataSet);
        barData.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                if (value == 0.001f) {
                    return entry.getData().toString();
                } else {
                    return "";
                }
            }
        });
        barData.setValueTextSize(14f);
        barData.setValueTextColor(Color.BLACK);
        barChartResults.setData(barData);
        barChartResults.animateY(500);
    }
}