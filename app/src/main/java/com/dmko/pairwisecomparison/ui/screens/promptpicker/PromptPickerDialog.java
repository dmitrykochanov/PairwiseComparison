package com.dmko.pairwisecomparison.ui.screens.promptpicker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dmko.pairwisecomparison.R;
import com.dmko.pairwisecomparison.ui.base.mvp.impl.BaseDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import timber.log.Timber;

public class PromptPickerDialog extends BaseDialogFragment {

    private OnPromptTextPickedListener listener;

    @BindView(R.id.input_name) TextInputLayout inputPrompt;

    private Unbinder unbinder;

    public interface OnPromptTextPickedListener {

        void onPromptPicked(String prompt);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            listener = ((OnPromptTextPickedListener) getTargetFragment());
        } catch (Throwable t) {
            Timber.e(t, "Target fragment must implement OnPromptTextPickedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_prompt_picker, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.button_ok)
    void onButtonOkClicked() {
        String prompt = inputPrompt.getEditText().getText().toString();

        if (prompt.trim().isEmpty()) {
            inputPrompt.setErrorEnabled(true);
            inputPrompt.setError(getString(R.string.error_empty_prompt_text));
        } else {
            listener.onPromptPicked(prompt);
            getDialog().cancel();
        }
    }

    @OnClick(R.id.button_cancel)
    void onButtonCancelClicked() {
        getDialog().cancel();
    }
}
