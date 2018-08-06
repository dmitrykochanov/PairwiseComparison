package com.dmko.pairwisecomparison.ui.screens.comparison.comparisonresult;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.dmko.pairwisecomparison.ui.screens.comparison.comparisonresult.recyclerview.ComparisonResultAdapter;
import com.dmko.pairwisecomparison.ui.screens.comparison.comparisonresult.spinner.ChartTypesAdapter;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
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
import timber.log.Timber;

public class ComparisonResultFragment extends BaseFragment implements ComparisonResultContract.View {

    private static final String ARG_COMP_ID = "comp_id";
    private static final String ARG_COMP_NAME = "comp_name";
    private static final int WRITE_EXTERNAL_STORAGE_CODE = 0;
    private static final int PIE_CHART = 0;
    private static final int BAR_CHART = 1;
    private static final int LIST = 2;

    @BindView(R.id.progress_loading) ProgressBar progressLoading;
    @BindView(R.id.chart_pie_results) PieChart pieChartResults;
    @BindView(R.id.chart_bar_results) HorizontalBarChart barChartResults;
    @BindView(R.id.text_empty_title) TextView textEmptyTitle;
    @BindView(R.id.text_empty_description) TextView textEmptyDescription;
    @BindView(R.id.spinner_chart_types) Spinner spinnerChartTypes;
    @BindView(R.id.recycler_comparison_results) RecyclerView recyclerResults;

    @Inject ComparisonResultContract.Presenter presenter;
    @Inject ComparisonResultAdapter adapter;
    @Inject ChartTypesAdapter spinnerAdapter;

    private Unbinder unbinder;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getControllerComponent().inject(this);
        presenter.attachView(this);

        if (getArguments() == null) {
            throw new IllegalArgumentException("no arguments");
        }
        String comparisonId = getArguments().getString(ARG_COMP_ID);
        String comparisonName = getArguments().getString(ARG_COMP_NAME);
        presenter.setArgs(comparisonId, comparisonName);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_comparison_result, container, false);
        unbinder = ButterKnife.bind(this, view);

        setupPieChart();
        setupBarChart();
        setupList();
        setupSpinner();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.stop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == WRITE_EXTERNAL_STORAGE_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            presenter.saveChartSelected(spinnerChartTypes.getSelectedItemPosition());
        }
    }

    @Override
    public void showLoading(boolean isLoading) {
        progressLoading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setResults(Map<Option, Integer> results, int defaultChart, int chartColor, boolean showPercentage) {
        int chartType = defaultChart;
        if (chartType == -1) {
            chartType = spinnerChartTypes.getSelectedItemPosition();
        } else {
            spinnerChartTypes.setSelection(chartType);
        }
        Timber.i("Setting results with chart type = %d", chartType);

        int[] colors;
        switch (chartColor) {
            case 0:
                colors = ColorTemplate.COLORFUL_COLORS;
                break;
            case 1:
                colors = ColorTemplate.JOYFUL_COLORS;
                break;
            case 2:
                colors = ColorTemplate.LIBERTY_COLORS;
                break;
            case 3:
                colors = ColorTemplate.MATERIAL_COLORS;
                break;
            case 4:
                colors = ColorTemplate.PASTEL_COLORS;
                break;
            case 5:
                colors = ColorTemplate.VORDIPLOM_COLORS;
                break;
            default:
                throw new IllegalArgumentException("Incorrect color: " + chartColor);
        }

        if (results.size() == 0) {

            pieChartResults.setVisibility(View.GONE);
            barChartResults.setVisibility(View.GONE);
            spinnerChartTypes.setVisibility(View.GONE);
            recyclerResults.setVisibility(View.GONE);
            textEmptyTitle.setVisibility(View.VISIBLE);
            textEmptyDescription.setVisibility(View.VISIBLE);
        } else if (chartType == PIE_CHART) {

            textEmptyTitle.setVisibility(View.GONE);
            textEmptyDescription.setVisibility(View.GONE);
            barChartResults.setVisibility(View.GONE);
            recyclerResults.setVisibility(View.GONE);
            spinnerChartTypes.setVisibility(View.VISIBLE);
            pieChartResults.setVisibility(View.VISIBLE);
            setResultsToPieChart(results, showPercentage, colors);
        } else if (chartType == BAR_CHART) {

            textEmptyTitle.setVisibility(View.GONE);
            textEmptyDescription.setVisibility(View.GONE);
            pieChartResults.setVisibility(View.GONE);
            recyclerResults.setVisibility(View.GONE);
            spinnerChartTypes.setVisibility(View.VISIBLE);
            barChartResults.setVisibility(View.VISIBLE);
            setResultsToBarChart(results, showPercentage, colors);
        } else if (chartType == LIST) {

            textEmptyTitle.setVisibility(View.GONE);
            textEmptyDescription.setVisibility(View.GONE);
            pieChartResults.setVisibility(View.GONE);
            barChartResults.setVisibility(View.GONE);
            spinnerChartTypes.setVisibility(View.VISIBLE);
            recyclerResults.setVisibility(View.VISIBLE);
            setResultsToList(results);
        }
    }

    @Override
    public void saveChart(int chartType, String comparisonName) {
        switch (chartType) {
            case PIE_CHART:
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_CODE);
                } else {
                    pieChartResults.saveToGallery(comparisonName + " pie chart " + System.currentTimeMillis(), 100);
                    Snackbar.make(spinnerChartTypes, R.string.snackbar_pie_chart_saved, Snackbar.LENGTH_LONG).show();
                }
                break;
            case BAR_CHART:
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_CODE);
                } else {
                    barChartResults.saveToGallery(comparisonName + " bar chart " + System.currentTimeMillis(), 100);
                    Snackbar.make(spinnerChartTypes, R.string.snackbar_bar_chart_saved, Snackbar.LENGTH_LONG).show();
                }
                break;
            case LIST:
                String results = presenter.convertResultsToText(adapter.getResults());
                ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setPrimaryClip(ClipData.newPlainText("Comparison results", results));
                Snackbar.make(spinnerChartTypes, R.string.snackbar_list_saved, Snackbar.LENGTH_LONG).show();
                break;
        }
    }

    private void setResultsToPieChart(Map<Option, Integer> results, boolean showPercentage, int[] colors) {
        List<PieEntry> pieEntries = new ArrayList<>(results.size());
        for (Option option : results.keySet()) {
            int progress = results.get(option);
            if (progress != 0) {
                pieEntries.add(new PieEntry(progress, option.getName()));
            }
        }
        Collections.sort(pieEntries, (p1, p2) -> Float.compare(p1.getValue(), p2.getValue()));

        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        dataSet.setColors(colors);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        PieData pieData = new PieData(dataSet);
        if (showPercentage) {
            pieData.setValueFormatter(new PercentFormatter());
            pieData.setValueTextSize(14f);
            pieData.setDrawValues(true);
        } else {
            pieData.setDrawValues(false);
        }
        pieChartResults.setData(pieData);
        pieChartResults.animateY(500);
    }

    private void setResultsToBarChart(Map<Option, Integer> results, boolean showPercentage, int[] colors) {
        List<BarEntry> barEntries = new ArrayList<>(results.size());
        List<Map.Entry<Option, Integer>> entries = new ArrayList<>(results.entrySet());
        Collections.sort(entries, (e1, e2) -> e1.getValue().compareTo(e2.getValue()));
        float y = 0;
        for (Map.Entry<Option, Integer> mapEntry : entries) {
            String data = " " + mapEntry.getKey().getName();
            if (showPercentage) {
                data += "(" + mapEntry.getValue() + "%)";
            }
            //Creating fake 0.001 bar to draw title to the right of it so it looks like the text is inside the main bar
            BarEntry entry = new BarEntry(y, new float[]{0.001f, mapEntry.getValue()}, data);
            barEntries.add(entry);
            y += 1;
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "");
        barDataSet.setColors(colors);
        barDataSet.setDrawValues(true);

        BarData barData = new BarData(barDataSet);
        barData.setValueFormatter((value, entry, dataSetIndex, viewPortHandler) -> {
            if (value == 0.001f) {
                return entry.getData().toString();
            } else {
                return "";
            }
        });
        barData.setValueTextSize(14f);
        barData.setValueTextColor(Color.BLACK);
        barChartResults.setData(barData);
        barChartResults.animateY(500);
    }

    private void setResultsToList(Map<Option, Integer> results) {
        List<Map.Entry<Option, Integer>> entries = new ArrayList<>(results.entrySet());
        Collections.sort(entries, Collections.reverseOrder((e1, e2) -> e1.getValue().compareTo(e2.getValue())));
        adapter.setResults(entries);
    }

    private void setupPieChart() {
        pieChartResults.setVisibility(View.GONE);
        pieChartResults.setDrawHoleEnabled(false);
        pieChartResults.setUsePercentValues(true);
        pieChartResults.setEntryLabelColor(Color.BLACK);
        pieChartResults.setEntryLabelTextSize(14f);
        pieChartResults.setDrawEntryLabels(true);
        pieChartResults.setDescription(null);
        pieChartResults.getLegend().setEnabled(false);
    }

    private void setupBarChart() {
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
    }

    private void setupList() {
        recyclerResults.setVisibility(View.GONE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerResults.setLayoutManager(layoutManager);
        recyclerResults.setAdapter(adapter);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), layoutManager.getOrientation());
        recyclerResults.addItemDecoration(itemDecoration);
    }

    private void setupSpinner() {
        spinnerChartTypes.setAdapter(spinnerAdapter);
        spinnerChartTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int chartType, long id) {
                presenter.onChartTypeChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public static ComparisonResultFragment newInstance(String comparisonId, String comparisonName) {
        Bundle args = new Bundle();
        args.putString(ARG_COMP_ID, comparisonId);
        args.putString(ARG_COMP_NAME, comparisonName);
        ComparisonResultFragment fragment = new ComparisonResultFragment();
        fragment.setArguments(args);
        return fragment;
    }
}