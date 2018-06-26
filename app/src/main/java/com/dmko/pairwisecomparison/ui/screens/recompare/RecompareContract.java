package com.dmko.pairwisecomparison.ui.screens.recompare;

import com.dmko.pairwisecomparison.data.entities.OptionComparisonEntry;
import com.dmko.pairwisecomparison.ui.base.mvp.BasePresenter;
import com.dmko.pairwisecomparison.ui.base.mvp.BaseView;

public interface RecompareContract {

    interface View extends BaseView {

        void setCurrentOptionComparison(OptionComparisonEntry optionComparison);

        void setPreviousEnabled(boolean enabled);

        void setNextEnabled(boolean enabled);

        void setProgress(String progress);

        void setDoneEnabled(boolean enabled);

    }

    interface Presenter extends BasePresenter<View> {

        void loadOptionComparisons(String comparisonId);

        void updateOptionComparison(int newProgress);

        void onPreviousSelected();

        void onNextSelected();
    }
}
