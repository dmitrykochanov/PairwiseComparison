package com.dmko.pairwisecomparison.ui.screens.comparison.options;

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
import com.dmko.pairwisecomparison.data.entities.Option;
import com.dmko.pairwisecomparison.ui.base.mvp.impl.BaseFragment;
import com.dmko.pairwisecomparison.ui.screens.addeditoption.AddEditOptionDialog;
import com.dmko.pairwisecomparison.ui.screens.comparison.options.recyclerview.OptionsAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class OptionsFragment extends BaseFragment implements OptionsContract.View {
    private static final String TAG_DIALOG = "dialog";
    private static final String ARG_COMP_ID = "comp_id";

    @BindView(R.id.recycler_options) RecyclerView recyclerOptions;
    @BindView(R.id.progress_loading) ProgressBar progressLoading;
    @BindView(R.id.text_empty_title) TextView textEmptyTitle;
    @BindView(R.id.text_empty_description) TextView textEmptyDescription;

    @Inject OptionsContract.Presenter presenter;
    @Inject OptionsAdapter adapter;

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
        View view = inflater.inflate(R.layout.fragment_options, container, false);
        unbinder = ButterKnife.bind(this, view);

        setupRecyclerView();
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
    public void setOptions(List<Option> options) {
        if (options.size() == 0) {
            recyclerOptions.setVisibility(View.GONE);
            textEmptyTitle.setVisibility(View.VISIBLE);
            textEmptyDescription.setVisibility(View.VISIBLE);
        } else {
            adapter.setOptions(options);
            recyclerOptions.setVisibility(View.VISIBLE);
            textEmptyTitle.setVisibility(View.GONE);
            textEmptyDescription.setVisibility(View.GONE);
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void showOptionDialog(String comparisonId, String optionId) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag(TAG_DIALOG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        DialogFragment dialog = AddEditOptionDialog.newInstance(comparisonId, optionId);
        dialog.show(ft, TAG_DIALOG);
    }

    public void onFabAddClicked() {
        presenter.addOptionSelected();
    }

    @SuppressWarnings("ConstantConditions")
    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerOptions.setLayoutManager(layoutManager);
        recyclerOptions.setAdapter(adapter);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), layoutManager.getOrientation());
        recyclerOptions.addItemDecoration(itemDecoration);
    }

    public static OptionsFragment newInstance(String comparisonId) {
        Bundle args = new Bundle();
        args.putString(ARG_COMP_ID, comparisonId);
        OptionsFragment fragment = new OptionsFragment();
        fragment.setArguments(args);
        return fragment;
    }
}