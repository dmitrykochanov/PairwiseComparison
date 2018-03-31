package com.dmko.pairwisecomparison.ui.base.mvp.impl;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dmko.pairwisecomparison.injection.controller.ControllerComponent;
import com.dmko.pairwisecomparison.injection.controller.ControllerModule;
import com.dmko.pairwisecomparison.injection.controller.PresenterModule;
import com.dmko.pairwisecomparison.ui.App;
import com.dmko.pairwisecomparison.ui.base.mvp.BaseView;

import timber.log.Timber;

import static com.dmko.pairwisecomparison.utils.LogTags.LOG_UI;

public class BaseDialogFragment extends DialogFragment implements BaseView {

    @SuppressWarnings("ConstantConditions")
    protected ControllerComponent getControllerComponent() {
        return ((App) getActivity().getApplication())
                .getApplicationComponent()
                .newControllerComponent(new PresenterModule(), new ControllerModule());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Timber.tag(LOG_UI);
        Timber.i("%s attached to %s", this.getClass().getSimpleName(), context.getClass().getSimpleName());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.tag(LOG_UI);
        Timber.i("%s is created", this.getClass().getSimpleName());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Timber.tag(LOG_UI);
        Timber.i("%s view created", this.getClass().getSimpleName());
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onStart() {
        super.onStart();
        Timber.tag(LOG_UI);
        Timber.i("%s started", this.getClass().getSimpleName());
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.tag(LOG_UI);
        Timber.i("%s resumed", this.getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        Timber.tag(LOG_UI);
        Timber.i("%s paused", this.getClass().getSimpleName());
    }

    @Override
    public void onStop() {
        super.onStop();
        Timber.tag(LOG_UI);
        Timber.i("%s stopped", this.getClass().getSimpleName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Timber.tag(LOG_UI);
        Timber.i("%s view destroyed", this.getClass().getSimpleName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Timber.tag(LOG_UI);
        Timber.i("%s destroyed", this.getClass().getSimpleName());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Timber.tag(LOG_UI);
        Timber.i("%s detached", this.getClass().getSimpleName());
    }
}
