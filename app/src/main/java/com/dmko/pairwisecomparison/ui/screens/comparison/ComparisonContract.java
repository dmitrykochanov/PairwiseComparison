package com.dmko.pairwisecomparison.ui.screens.comparison;

import com.dmko.pairwisecomparison.ui.base.mvp.BasePresenter;
import com.dmko.pairwisecomparison.ui.base.mvp.BaseView;

public interface ComparisonContract {

    interface View extends BaseView {

        void showNothingToCompareDialog();

        void openRecompareActivity(String comparisonId, String comparisonName);
    }

    interface Presenter extends BasePresenter<View> {

        void onOpenRecompareActivitySelected(String comparisonId, String comparisonName);
    }
}
