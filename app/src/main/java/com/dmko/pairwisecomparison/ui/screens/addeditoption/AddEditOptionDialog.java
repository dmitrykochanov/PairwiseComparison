package com.dmko.pairwisecomparison.ui.screens.addeditoption;

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
import android.widget.TextView;

import com.dmko.pairwisecomparison.R;
import com.dmko.pairwisecomparison.data.entities.Option;
import com.dmko.pairwisecomparison.ui.base.mvp.impl.BaseDialogFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.Unbinder;

public class AddEditOptionDialog extends BaseDialogFragment implements AddEditOptionContract.View {
    private static final String ARG_OPTION_ID = "option_id";
    private static final String ARG_COMP_ID = "comp_id";

    @BindView(R.id.input_name) TextInputLayout inputName;
    @BindView(R.id.progress_loading) ProgressBar progressLoading;
    @BindView(R.id.text_title) TextView textTitle;

    @Inject AddEditOptionContract.Presenter presenter;

    private Unbinder unbinder;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getControllerComponent().inject(this);
        presenter.attachView(this);
    }

    @Nullable
    @Override
    @SuppressWarnings("ConstantConditions")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_add_edit_option, container, false);
        unbinder = ButterKnife.bind(this, view);

        String comparisonId = getArguments().getString(ARG_COMP_ID);
        String optionId = getArguments().getString(ARG_OPTION_ID);
        presenter.start(comparisonId, optionId);
        if (optionId != null) {
            textTitle.setText(R.string.title_edit_option);
        }

        Window window = getDialog().getWindow();
        if (window != null) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            window.requestFeature(Window.FEATURE_NO_TITLE);
        }

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
    @SuppressWarnings("ConstantConditions")
    public void onButtonOkClicked() {
        String newName = inputName.getEditText().getText().toString().trim();
        if (newName.isEmpty()) {
            inputName.setErrorEnabled(true);
            inputName.setError(getString(R.string.error_empty_option_name));
        } else {
            presenter.saveOption(newName);
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
    @SuppressWarnings("ConstantConditions")
    public void setOption(Option option) {
        String name = option.getName();
        if (name != null) {
            inputName.getEditText().setText(name);
            inputName.getEditText().setSelection(name.length());
        }
    }

    public static AddEditOptionDialog newInstance(String comparisonId, String optionId) {
        Bundle args = new Bundle();
        args.putString(ARG_OPTION_ID, optionId);
        args.putString(ARG_COMP_ID, comparisonId);
        AddEditOptionDialog fragment = new AddEditOptionDialog();
        fragment.setArguments(args);
        return fragment;
    }
}