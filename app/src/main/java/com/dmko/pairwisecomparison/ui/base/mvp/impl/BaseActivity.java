package com.dmko.pairwisecomparison.ui.base.mvp.impl;


import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.dmko.pairwisecomparison.injection.controller.ControllerComponent;
import com.dmko.pairwisecomparison.injection.controller.ControllerModule;
import com.dmko.pairwisecomparison.injection.controller.PresenterModule;
import com.dmko.pairwisecomparison.ui.App;
import com.dmko.pairwisecomparison.ui.base.mvp.BaseView;


public abstract class BaseActivity extends AppCompatActivity implements BaseView {

    private static final String TAG_DIALOG = "dialog";

    protected ControllerComponent getControllerComponent() {
        return ((App) getApplication())
                .getApplicationComponent()
                .newControllerComponent(new PresenterModule(), new ControllerModule());
    }

    protected void showDialog(DialogFragment dialog) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(TAG_DIALOG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        dialog.show(ft, TAG_DIALOG);
    }
}
