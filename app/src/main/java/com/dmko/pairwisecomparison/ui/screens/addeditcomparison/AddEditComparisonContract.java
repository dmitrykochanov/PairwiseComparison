package com.dmko.pairwisecomparison.ui.screens.addeditcomparison;

import com.dmko.pairwisecomparison.data.entities.Comparison;
import com.dmko.pairwisecomparison.ui.base.mvp.BasePresenter;
import com.dmko.pairwisecomparison.ui.base.mvp.BaseView;

public interface AddEditComparisonContract {

    interface View extends BaseView {

        void showLoading(boolean isLoading);

        void setComparison(Comparison comparison);

        void showEmptyNameError();

        void closeDialog();
    }

    interface Presenter extends BasePresenter<View> {

        void setArgs(String comparisonId);

        void start();

        void saveComparison(String comparisonName);
    }
}