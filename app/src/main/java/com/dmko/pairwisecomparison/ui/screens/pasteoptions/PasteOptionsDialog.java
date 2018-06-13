package com.dmko.pairwisecomparison.ui.screens.pasteoptions;

import android.app.DialogFragment;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.test.mock.MockApplication;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dmko.pairwisecomparison.R;
import com.dmko.pairwisecomparison.data.entities.Option;
import com.dmko.pairwisecomparison.ui.App;
import com.dmko.pairwisecomparison.ui.base.mvp.impl.BaseDialogFragment;
import com.dmko.pairwisecomparison.ui.screens.pasteoptions.recyclerview.PasteOptionsAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PasteOptionsDialog extends BaseDialogFragment implements PasteOptionsContract.View {

    private static final String ARG_COMPARISON_ID = "comparson_id";

    @BindView(R.id.recycler_options) RecyclerView recyclerOptions;

    @Inject PasteOptionsContract.Presenter presenter;
    @Inject PasteOptionsAdapter adapter;

    private Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getControllerComponent().inject(this);
        presenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_paste_options, container, false);
        unbinder = ButterKnife.bind(this, view);

        if (getArguments() == null) {
            throw new IllegalArgumentException("no arguments");
        }
        String comparisonId = getArguments().getString(ARG_COMPARISON_ID);

        recyclerOptions.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerOptions.setAdapter(adapter);

        ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        String clipboardContent = clipboardManager.getPrimaryClip().getItemAt(0).getText().toString();
        presenter.start(comparisonId, clipboardContent);

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

    @OnClick(R.id.button_save)
    public void onSaveButtonClicked() {
        presenter.saveOptions();
    }

    @OnClick(R.id.button_cancel)
    public void onCancelButtonClicked() {
        closeDialog();
    }

    @Override
    public void setOptions(List<Option> options) {
        adapter.setOptions(options);
    }

    @Override
    public void closeDialog() {
        getDialog().cancel();
    }

    public static PasteOptionsDialog newInstance(String comparisonId) {
        Bundle args = new Bundle();
        args.putString(ARG_COMPARISON_ID, comparisonId);
        PasteOptionsDialog fragment = new PasteOptionsDialog();
        fragment.setArguments(args);
        return fragment;
    }
}
