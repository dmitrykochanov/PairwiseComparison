package com.dmko.pairwisecomparison.ui.screens.addeditoption;

import com.dmko.pairwisecomparison.data.entities.Option;
import com.dmko.pairwisecomparison.ui.base.mvp.BasePresenter;
import com.dmko.pairwisecomparison.ui.base.mvp.BaseView;

public interface AddEditOptionContract {

    interface View extends BaseView {

        void showLoading(boolean isLoading);

        void setOption(Option option);

        void showEmptyNameError();

        void closeDialog();
    }

    interface Presenter extends BasePresenter<View> {

        void setArgs(String comparisonId, String optionId);

        void start();

        void saveOption(String optionName);
    }
}