package com.dmko.pairwisecomparison.ui.screens.recompare;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dmko.pairwisecomparison.R;
import com.dmko.pairwisecomparison.data.entities.OptionComparisonEntry;
import com.dmko.pairwisecomparison.ui.base.mvp.impl.BaseFragment;
import com.dmko.pairwisecomparison.ui.screens.promptpicker.PromptPickerDialog;
import com.dmko.pairwisecomparison.ui.views.OptionComparisonView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class RecompareFragment extends BaseFragment implements RecompareContract.View, PromptPickerDialog.OnPromptTextPickedListener {

    private static final String ARG_COMPARISON_ID = "comparison_id";

    @BindView(R.id.view_option_comparison) OptionComparisonView viewOptionComparison;
    @BindView(R.id.image_next) ImageView imageNext;
    @BindView(R.id.image_prev) ImageView imagePrevious;
    @BindView(R.id.image_done) ImageView imageDone;
    @BindView(R.id.text_progress) TextView textProgress;
    @BindView(R.id.text_prompt) TextView textPrompt;

    @Inject RecompareContract.Presenter presenter;

    private Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getControllerComponent().inject(this);
        presenter.attachView(this);

        if (getArguments() == null) {
            throw new IllegalArgumentException("No arguments");
        }
        String comparisonId = getArguments().getString(ARG_COMPARISON_ID);
        presenter.setArgs(comparisonId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recompare, container, false);
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

    @OnClick(R.id.image_next)
    void onNextClicked() {
        presenter.onNextSelected();
    }

    @OnClick(R.id.image_prev)
    void onPreviousClicked() {
        presenter.onPreviousSelected();
    }

    @OnClick(R.id.image_done)
    void onDoneClicked() {
        requireActivity().finish();
    }

    @OnClick(R.id.text_prompt)
    void onSelectPromptClicked() {
        DialogFragment dialog = new PromptPickerDialog();
        dialog.setTargetFragment(this, 0);
        showDialog(dialog);
    }

    @Override
    public void setCurrentOptionComparison(OptionComparisonEntry optionComparison) {
        viewOptionComparison.setOnProgressChangedListener(null);
        viewOptionComparison.init(optionComparison);
        viewOptionComparison.setOnProgressChangedListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int newProgress = seekBar.getProgress() - seekBar.getMax() / 2;
                presenter.updateOptionComparison(newProgress);
            }
        });
    }

    @Override
    public void setPreviousEnabled(boolean enabled) {
        imagePrevious.setVisibility(enabled ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void setNextEnabled(boolean enabled) {
        imageNext.setVisibility(enabled ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void setProgress(String progress) {
        textProgress.setText(progress);
    }

    @Override
    public void setDoneEnabled(boolean enabled) {
        imageDone.setVisibility(enabled ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setPromptText(String prompt) {
        textPrompt.setText(prompt);
    }

    @Override
    public String getDefaultPromptText() {
        return getString(R.string.title_prompt_default);
    }

    @Override
    public void onPromptPicked(String prompt) {
        presenter.savePromptText(prompt);
    }

    public static RecompareFragment newInstance(String comparisonId) {
        Bundle args = new Bundle();
        args.putString(ARG_COMPARISON_ID, comparisonId);
        RecompareFragment fragment = new RecompareFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
