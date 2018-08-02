package com.dmko.pairwisecomparison.ui.screens.comparison.options;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
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
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.Unbinder;

public class OptionsFragment extends BaseFragment implements OptionsContract.View {

    private static final String TAG_DIALOG = "dialog";
    private static final String ARG_COMP_ID = "comp_id";

    @BindView(R.id.recycler_options) RecyclerView recyclerOptions;
    @BindView(R.id.progress_loading) ProgressBar progressLoading;
    @BindView(R.id.text_empty_title) TextView textEmptyTitle;
    @BindView(R.id.text_empty_description) TextView textEmptyDescription;
    @BindView(R.id.button_add) Button buttonAdd;
    @BindView(R.id.input_name) TextInputLayout inputName;

    @Inject OptionsContract.Presenter presenter;
    @Inject OptionsAdapter adapter;

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
        presenter.setArgs(comparisonId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_options, container, false);
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


    @OnClick(R.id.button_add)
    public void onButtonAddClicked() {
        String optionName = inputName.getEditText().getText().toString();
        if (optionName.trim().isEmpty()) {
            inputName.setErrorEnabled(true);
            inputName.setError(getString(R.string.error_empty_option_name));
        } else {
            inputName.setErrorEnabled(false);
            inputName.getEditText().setText("");
            presenter.addOption(optionName);
        }
    }

    @OnEditorAction(R.id.input_text_name)
    boolean onEditorAction(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            onButtonAddClicked();
        }
        return true;
    }

    @Override
    public void showLoading(boolean isLoading) {
        progressLoading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setOptions(List<Option> options) {
        adapter.setOptions(options);
        recyclerOptions.setVisibility(View.VISIBLE);
        textEmptyTitle.setVisibility(View.GONE);
        textEmptyDescription.setVisibility(View.GONE);
    }

    @Override
    public void setEmptyOptions() {
        recyclerOptions.setVisibility(View.GONE);
        textEmptyTitle.setVisibility(View.VISIBLE);
        textEmptyDescription.setVisibility(View.VISIBLE);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void showOptionDialog(String comparisonId, String optionId) {
        DialogFragment dialog = AddEditOptionDialog.newInstance(comparisonId, optionId);
        showDialog(dialog);
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