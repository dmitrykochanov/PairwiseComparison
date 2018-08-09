package com.dmko.pairwisecomparison.ui.screens.copycomparison;

import com.dmko.pairwisecomparison.data.entities.Comparison;
import com.dmko.pairwisecomparison.ui.base.mvp.BasePresenter;
import com.dmko.pairwisecomparison.ui.base.mvp.BaseView;

public interface CopyComparisonContract {

    interface View extends BaseView {

        void setComparison(Comparison comparison);

        void showLoading(boolean isLoading);

        void closeDialog();
    }

    interface Presenter extends BasePresenter<View> {

        void setArgs(String comparisonId);

        void start();

        void onButtonOkClicked(String newName, boolean copyOptionComparisons);
    }
}
