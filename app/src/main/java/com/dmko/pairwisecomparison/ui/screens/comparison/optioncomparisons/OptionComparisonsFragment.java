package com.dmko.pairwisecomparison.ui.screens.comparison.optioncomparisons;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.dmko.pairwisecomparison.data.entities.OptionComparisonEntry;
import com.dmko.pairwisecomparison.ui.base.mvp.impl.BaseFragment;
import com.dmko.pairwisecomparison.ui.screens.comparison.optioncomparisons.filters.AllFilter;
import com.dmko.pairwisecomparison.ui.screens.comparison.optioncomparisons.filters.OptionComparisonEntryFilter;
import com.dmko.pairwisecomparison.ui.screens.comparison.optioncomparisons.recyclerview.OptionComparisonsAdapter;
import com.dmko.pairwisecomparison.ui.screens.comparison.optioncomparisons.spinnerfilter.FilterTypesAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class OptionComparisonsFragment extends BaseFragment implements OptionComparisonsContract.View {

    private static final String ARG_COMP_ID = "comp_id";

    @BindView(R.id.recycler_option_comparisons) RecyclerView recyclerOptionComparisons;
    @BindView(R.id.progress_loading) ProgressBar progressLoading;
    @BindView(R.id.text_empty_title) TextView textEmptyTitle;
    @BindView(R.id.text_empty_description) TextView textEmptyDescription;
    @BindView(R.id.spinner_filter_types) Spinner spinnerFilterTypes;

    @Inject OptionComparisonsContract.Presenter presenter;
    @Inject OptionComparisonsAdapter adapterOptionComparisons;
    @Inject FilterTypesAdapter adapterFilterTypes;

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
        presenter.setArgs(comparisonId, getResources());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_option_comparisons, container, false);
        unbinder = ButterKnife.bind(this, view);

        setupRecyclerView();
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
    public void showLoading(boolean isLoading) {
        progressLoading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setEmptyOptionComparisons() {
        recyclerOptionComparisons.setVisibility(View.GONE);
        textEmptyTitle.setVisibility(View.VISIBLE);
        textEmptyDescription.setVisibility(View.VISIBLE);
    }

    @Override
    public void setOptionComparisons(List<OptionComparisonEntry> optionComparisons) {
        adapterOptionComparisons.setOptionComparisons(optionComparisons);
        recyclerOptionComparisons.setVisibility(View.VISIBLE);
        textEmptyTitle.setVisibility(View.GONE);
        textEmptyDescription.setVisibility(View.GONE);
    }

    @Override
    public void setFilterTypes(List<OptionComparisonEntryFilter> filters) {
        if (filters.size() == 2) {
            spinnerFilterTypes.setVisibility(View.GONE);
            presenter.setFilter(new AllFilter(getResources()));
        } else {
            spinnerFilterTypes.setVisibility(View.VISIBLE);
            adapterFilterTypes.setFilterTypes(filters);
            OptionComparisonEntryFilter filter = (OptionComparisonEntryFilter) spinnerFilterTypes.getSelectedItem();
            if (filter != null) {
                presenter.setFilter(filter);
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerOptionComparisons.setLayoutManager(layoutManager);
        recyclerOptionComparisons.setAdapter(adapterOptionComparisons);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), layoutManager.getOrientation());
        recyclerOptionComparisons.addItemDecoration(itemDecoration);
    }

    private void setupSpinner() {
        spinnerFilterTypes.setAdapter(adapterFilterTypes);
        spinnerFilterTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                OptionComparisonEntryFilter filter = (OptionComparisonEntryFilter) adapterView.getItemAtPosition(pos);
                presenter.setFilter(filter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public static OptionComparisonsFragment newInstance(String comparisonId) {
        Bundle args = new Bundle();
        args.putString(ARG_COMP_ID, comparisonId);
        OptionComparisonsFragment fragment = new OptionComparisonsFragment();
        fragment.setArguments(args);
        return fragment;
    }
}