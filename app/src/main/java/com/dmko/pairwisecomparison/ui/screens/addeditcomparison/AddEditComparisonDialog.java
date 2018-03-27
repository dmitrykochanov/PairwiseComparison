package com.dmko.pairwisecomparison.ui.screens.addeditcomparison;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
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

public class AddEditComparisonDialog extends BaseDialogFragment implements AddEditComparisonContract.View {
    private static final String ARG_COMP_ID = "comp_id";
    @BindView(R.id.input_name) TextInputLayout inputName;
    @BindView(R.id.progress_loading) ProgressBar progressLoading;

    @Inject AddEditComparisonContract.Presenter presenter;

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
        View view = inflater.inflate(R.layout.dialog_add_edit_comparison, container, false);
        unbinder = ButterKnife.bind(this, view);

        String comparisonId = getArguments().getString(ARG_COMP_ID);
        presenter.start(comparisonId);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

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

    @OnClick(R.id.button_cancel)
    public void onButtonCancelClicked() {
        getDialog().cancel();
    }

    @OnClick(R.id.button_ok)
    public void onButtonOkClicked() {
        String newName = inputName.getEditText().getText().toString().trim();
        if (newName.isEmpty()) {
            inputName.setErrorEnabled(true);
            inputName.setError(getString(R.string.error_empty_comparison_name));
        } else {
            presenter.saveComparison(newName);
            getDialog().cancel();
        }
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
    public void setComparison(Comparison comparison) {
        String name = comparison.getName();
        if (name != null) {
            inputName.getEditText().setText(name);
            inputName.getEditText().setSelection(name.length());
        }
    }

    public static AddEditComparisonDialog newInstance(String comparisonId) {
        Bundle args = new Bundle();
        args.putString(ARG_COMP_ID, comparisonId);
        AddEditComparisonDialog fragment = new AddEditComparisonDialog();
        fragment.setArguments(args);
        return fragment;
    }
}