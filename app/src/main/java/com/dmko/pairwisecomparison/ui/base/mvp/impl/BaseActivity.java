package com.dmko.pairwisecomparison.ui.base.mvp.impl;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.dmko.pairwisecomparison.injection.controller.ControllerComponent;
import com.dmko.pairwisecomparison.injection.controller.ControllerModule;
import com.dmko.pairwisecomparison.injection.controller.PresenterModule;
import com.dmko.pairwisecomparison.ui.App;
import com.dmko.pairwisecomparison.ui.base.mvp.BaseView;

import static com.dmko.pairwisecomparison.utils.LogTags.LOG_UI;

public class BaseActivity extends AppCompatActivity implements BaseView {
    protected ControllerComponent getControllerComponent() {
        return ((App) getApplication())
                .getApplicationComponent()
                .newControllerComponent(new PresenterModule(), new ControllerModule());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupLogs();
    }

    private void setupLogs() {
        getSupportFragmentManager().registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentAttached(FragmentManager fm, Fragment f, Context context) {
                super.onFragmentAttached(fm, f, context);
                Log.i(LOG_UI, f.getClass().getSimpleName() + " attached to " + f.getActivity().getClass().getSimpleName());
            }


            @Override
            public void onFragmentCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
                super.onFragmentCreated(fm, f, savedInstanceState);
                Log.i(LOG_UI, f.getClass().getSimpleName() + " created");
            }


            @Override
            public void onFragmentViewCreated(FragmentManager fm, Fragment f, View v, Bundle savedInstanceState) {
                super.onFragmentViewCreated(fm, f, v, savedInstanceState);
                Log.i(LOG_UI, f.getClass().getSimpleName() + " view created");
            }

            @Override
            public void onFragmentStarted(FragmentManager fm, Fragment f) {
                super.onFragmentStarted(fm, f);
                Log.i(LOG_UI, f.getClass().getSimpleName() + " started");
            }

            @Override
            public void onFragmentResumed(FragmentManager fm, Fragment f) {
                super.onFragmentResumed(fm, f);
                Log.i(LOG_UI, f.getClass().getSimpleName() + " resumed");
            }

            @Override
            public void onFragmentPaused(FragmentManager fm, Fragment f) {
                super.onFragmentPaused(fm, f);
                Log.i(LOG_UI, f.getClass().getSimpleName() + " paused");
            }

            @Override
            public void onFragmentStopped(FragmentManager fm, Fragment f) {
                super.onFragmentStopped(fm, f);
                Log.i(LOG_UI, f.getClass().getSimpleName() + " stopped");
            }

            @Override
            public void onFragmentSaveInstanceState(FragmentManager fm, Fragment f, Bundle outState) {
                super.onFragmentSaveInstanceState(fm, f, outState);
                Log.i(LOG_UI, f.getClass().getSimpleName() + " saved");
            }

            @Override
            public void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
                super.onFragmentViewDestroyed(fm, f);
                Log.i(LOG_UI, f.getClass().getSimpleName() + " view destroyed");
            }

            @Override
            public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
                super.onFragmentDestroyed(fm, f);
                Log.i(LOG_UI, f.getClass().getSimpleName() + " destroyed");
            }

            @Override
            public void onFragmentDetached(FragmentManager fm, Fragment f) {
                super.onFragmentDetached(fm, f);
                Log.i(LOG_UI, f.getClass().getSimpleName() + " detached from " + f.getActivity().getClass().getSimpleName());
            }
        }, true);
    }
}
