package com.dmko.pairwisecomparison.ui.screens.comparison.comparisonresult;

import com.dmko.pairwisecomparison.data.entities.Option;
import com.dmko.pairwisecomparison.ui.base.mvp.BasePresenter;
import com.dmko.pairwisecomparison.ui.base.mvp.BaseView;

import java.util.Map;

public interface ComparisonResultContract {
    interface View extends BaseView {

        void showLoading(boolean isLoading);

        void setResults(Map<Option, Integer> results);
    }

    interface Presenter extends BasePresenter<View> {

        void start(String comparisonId);
    }
}