package com.dmko.pairwisecomparison.ui.screens.copycomparison;

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
import android.widget.CheckBox;
import android.widget.ProgressBar;

import com.dmko.pairwisecomparison.R;
import com.dmko.pairwisecomparison.data.entities.Comparison;
import com.dmko.pairwisecomparison.ui.base.mvp.impl.BaseDialogFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.Unbinder;

public class CopyComparisonDialog extends BaseDialogFragment implements CopyComparisonContract.View {

    private static final String ARG_COMPARISON_ID = "comparison_id";

    @BindView(R.id.input_old_name) TextInputLayout inputOldName;
    @BindView(R.id.input_new_name) TextInputLayout inputNewName;
    @BindView(R.id.progress_loading) ProgressBar progressLoading;
    @BindView(R.id.check_copy_option_comparisons) CheckBox checkCopyOptionComparisons;

    @Inject CopyComparisonContract.Presenter presenter;

    private Unbinder unbinder;
    private InputMethodManager inputMethodManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getControllerComponent().inject(this);
        presenter.attachView(this);

        if (getArguments() == null) {
            throw new IllegalArgumentException("no arguments");
        }
        String comparisonId = getArguments().getString(ARG_COMPARISON_ID);
        presenter.setArgs(comparisonId);

        inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_copy_comparison, container, false);
        unbinder = ButterKnife.bind(this, view);

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
    public void onButtonOkClicked() {
        presenter.onButtonOkClicked(inputNewName.getEditText().getText().toString(), checkCopyOptionComparisons.isChecked());
    }

    @OnEditorAction(R.id.input_text_new_name)
    boolean onEditorAction(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            onButtonOkClicked();
        }
        return true;
    }

    @Override
    public void setComparison(Comparison comparison) {
        inputOldName.getEditText().setText(comparison.getName());
        inputNewName.getEditText().setText(comparison.getName());
        inputNewName.getEditText().requestFocus();
        inputNewName.getEditText().setSelection(comparison.getName().length());
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    @Override
    public void showLoading(boolean isLoading) {
        progressLoading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void closeDialog() {
        inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        getDialog().cancel();
    }

    public static CopyComparisonDialog newInstance(String comparisonId) {
        Bundle args = new Bundle();
        args.putString(ARG_COMPARISON_ID, comparisonId);
        CopyComparisonDialog fragment = new CopyComparisonDialog();
        fragment.setArguments(args);
        return fragment;
    }
}
