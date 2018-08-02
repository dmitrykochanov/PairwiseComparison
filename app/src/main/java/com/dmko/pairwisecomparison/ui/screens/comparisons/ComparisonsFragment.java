package com.dmko.pairwisecomparison.ui.screens.comparisons;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dmko.pairwisecomparison.R;
import com.dmko.pairwisecomparison.data.entities.Comparison;
import com.dmko.pairwisecomparison.ui.base.mvp.impl.BaseFragment;
import com.dmko.pairwisecomparison.ui.screens.addeditcomparison.AddEditComparisonDialog;
import com.dmko.pairwisecomparison.ui.screens.comparison.ComparisonActivity;
import com.dmko.pairwisecomparison.ui.screens.comparisons.recyclerview.ComparisonsAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ComparisonsFragment extends BaseFragment implements ComparisonsContract.View {

    @BindView(R.id.recycler_comparisons) RecyclerView recyclerComparisons;
    @BindView(R.id.progress_loading) ProgressBar progressLoading;
    @BindView(R.id.text_empty_title) TextView textEmptyTitle;
    @BindView(R.id.text_empty_description) TextView textEmptyDescription;

    @Inject ComparisonsContract.Presenter presenter;
    @Inject ComparisonsAdapter adapter;

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
        View view = inflater.inflate(R.layout.fragment_comparisons, container, false);
        unbinder = ButterKnife.bind(this, view);
        setupRecyclerView();
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
    public void setComparisons(List<Comparison> comparisons) {
        adapter.setComparisons(comparisons);
        recyclerComparisons.setVisibility(View.VISIBLE);
        textEmptyTitle.setVisibility(View.GONE);
        textEmptyDescription.setVisibility(View.GONE);
    }

    @Override
    public void setEmptyComparisons() {
        recyclerComparisons.setVisibility(View.GONE);
        textEmptyTitle.setVisibility(View.VISIBLE);
        textEmptyDescription.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoading(boolean isLoading) {
        progressLoading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void showAddEditComparisonDialog(String comparisonId) {
        DialogFragment dialog = AddEditComparisonDialog.newInstance(comparisonId);
        showDialog(dialog);
    }

    @Override
    public void openComparison(Comparison comparison) {
        Intent intent = ComparisonActivity.getIntent(getContext(), comparison.getId(), comparison.getName());
        startActivity(intent);
    }

    public void onFabAddClicked() {
        presenter.addComparisonSelected();
    }

    @SuppressWarnings("ConstantConditions")
    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerComparisons.setLayoutManager(layoutManager);
        recyclerComparisons.setAdapter(adapter);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), layoutManager.getOrientation());
        recyclerComparisons.addItemDecoration(itemDecoration);
    }
}