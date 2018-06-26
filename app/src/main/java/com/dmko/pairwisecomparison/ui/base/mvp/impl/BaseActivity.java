package com.dmko.pairwisecomparison.ui.base.mvp.impl;


import android.support.v7.app.AppCompatActivity;

import com.dmko.pairwisecomparison.injection.controller.ControllerComponent;
import com.dmko.pairwisecomparison.injection.controller.ControllerModule;
import com.dmko.pairwisecomparison.injection.controller.PresenterModule;
import com.dmko.pairwisecomparison.ui.App;
import com.dmko.pairwisecomparison.ui.base.mvp.BaseView;


public abstract class BaseActivity extends AppCompatActivity implements BaseView {

    protected ControllerComponent getControllerComponent() {
        return ((App) getApplication())
                .getApplicationComponent()
                .newControllerComponent(new PresenterModule(), new ControllerModule());
    }
}
