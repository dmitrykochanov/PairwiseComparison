package com.dmko.pairwisecomparison.ui.base.mvp.impl;


import android.support.v4.app.DialogFragment;

import com.dmko.pairwisecomparison.injection.controller.ControllerComponent;
import com.dmko.pairwisecomparison.injection.controller.ControllerModule;
import com.dmko.pairwisecomparison.injection.controller.PresenterModule;
import com.dmko.pairwisecomparison.ui.App;
import com.dmko.pairwisecomparison.ui.base.mvp.BaseView;

public class BaseDialogFragment extends DialogFragment implements BaseView {
    protected ControllerComponent getControllerComponent() {
        return ((App) getActivity().getApplication())
                .getApplicationComponent()
                .newControllerComponent(new PresenterModule(), new ControllerModule());
    }
}
