package com.dmko.pairwisecomparison.ui.screens.comparison.optioncomparisons;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dmko.pairwisecomparison.R;
import com.dmko.pairwisecomparison.data.entities.OptionComparisonEntry;
import com.dmko.pairwisecomparison.ui.base.mvp.impl.BaseFragment;
import com.dmko.pairwisecomparison.ui.screens.addeditoption.AddEditOptionDialog;
import com.dmko.pairwisecomparison.ui.screens.comparison.optioncomparisons.recyclerview.OptionComparisonsAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class OptionComparisonsFragment extends BaseFragment implements OptionComparisonsContract.View {
    private static final String TAG_DIALOG = "dialog";
    private static final String ARG_COMP_ID = "comp_id";

    @BindView(R.id.recycler_option_comparisons) RecyclerView recyclerOptionComparisons;
    @BindView(R.id.progress_loading) ProgressBar progressLoading;
    @BindView(R.id.text_empty_title) TextView textEmptyTitle;
    @BindView(R.id.text_empty_description) TextView textEmptyDescription;

    @Inject OptionComparisonsContract.Presenter presenter;
    @Inject OptionComparisonsAdapter adapter;

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
        View view = inflater.inflate(R.layout.fragment_option_comparisons, container, false);
        unbinder = ButterKnife.bind(this, view);

        String comparisonId = null;
        if (getArguments() != null) {
            comparisonId = getArguments().getString(ARG_COMP_ID);
        }
        setupRecyclerView();
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

    public void onFabAddClicked() {
        presenter.onAddOptionSelected();
    }

    @Override
    public void showLoading(boolean isLoading) {
        progressLoading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setOptionComparisons(List<OptionComparisonEntry> optionComparisons) {
        if (optionComparisons.size() == 0) {
            recyclerOptionComparisons.setVisibility(View.GONE);
            textEmptyTitle.setVisibility(View.VISIBLE);
            textEmptyDescription.setVisibility(View.VISIBLE);
        } else {
            adapter.setOptionComparisons(optionComparisons);
            recyclerOptionComparisons.setVisibility(View.VISIBLE);
            textEmptyTitle.setVisibility(View.GONE);
            textEmptyDescription.setVisibility(View.GONE);
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void showOptionDialog(String comparisonId) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag(TAG_DIALOG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        DialogFragment dialog = AddEditOptionDialog.newInstance(comparisonId, null);
        dialog.show(ft, TAG_DIALOG);
    }

    @SuppressWarnings("ConstantConditions")
    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerOptionComparisons.setLayoutManager(layoutManager);
        recyclerOptionComparisons.setAdapter(adapter);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), layoutManager.getOrientation());
        recyclerOptionComparisons.addItemDecoration(itemDecoration);
    }

    public static OptionComparisonsFragment newInstance(String comparisonId) {
        Bundle args = new Bundle();
        args.putString(ARG_COMP_ID, comparisonId);
        OptionComparisonsFragment fragment = new OptionComparisonsFragment();
        fragment.setArguments(args);
        return fragment;
    }
}