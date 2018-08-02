package com.dmko.pairwisecomparison.ui.screens.addeditcomparison;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dmko.pairwisecomparison.R;
import com.dmko.pairwisecomparison.data.entities.Comparison;
import com.dmko.pairwisecomparison.ui.base.mvp.impl.BaseDialogFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.Unbinder;

public class AddEditComparisonDialog extends BaseDialogFragment implements AddEditComparisonContract.View {

    private static final String ARG_COMP_ID = "comp_id";

    @BindView(R.id.input_name) TextInputLayout inputName;
    @BindView(R.id.progress_loading) ProgressBar progressLoading;
    @BindView(R.id.text_title) TextView textTitle;

    @Inject AddEditComparisonContract.Presenter presenter;

    private Unbinder unbinder;
    private InputMethodManager inputMethodManager;

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

        inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Nullable
    @Override
    @SuppressWarnings("ConstantConditions")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_add_edit_comparison, container, false);
        unbinder = ButterKnife.bind(this, view);

        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        inputName.getEditText().requestFocus();

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

    @OnClick(R.id.button_cancel)
    public void onButtonCancelClicked() {
        closeDialog();
    }

    @OnClick(R.id.button_ok)
    @SuppressWarnings("ConstantConditions")
    public void onButtonOkClicked() {
        String newName = inputName.getEditText().getText().toString().trim();
        presenter.saveComparison(newName);
    }

    @OnEditorAction(R.id.input_text_name)
    boolean onEditorAction(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            onButtonOkClicked();
        }
        return true;
    }

    @Override
    public void showLoading(boolean isLoading) {
        progressLoading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void setComparison(Comparison comparison) {
        textTitle.setText(R.string.title_edit_comparison);
        String name = comparison.getName();
        inputName.getEditText().setText(name);
        inputName.getEditText().setSelection(name.length());
    }

    @Override
    public void showEmptyNameError() {
        inputName.setErrorEnabled(true);
        inputName.setError(getString(R.string.error_empty_comparison_name));
    }

    @Override
    public void closeDialog() {
        inputMethodManager.hideSoftInputFromWindow(inputName.getEditText().getWindowToken(), 0);
        getDialog().cancel();
    }

    public static AddEditComparisonDialog newInstance(String comparisonId) {
        Bundle args = new Bundle();
        args.putString(ARG_COMP_ID, comparisonId);
        AddEditComparisonDialog fragment = new AddEditComparisonDialog();
        fragment.setArguments(args);
        return fragment;
    }
}