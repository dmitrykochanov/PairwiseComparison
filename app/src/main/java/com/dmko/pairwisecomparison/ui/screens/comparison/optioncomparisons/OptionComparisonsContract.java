package com.dmko.pairwisecomparison.ui.screens.comparison.optioncomparisons;

import com.dmko.pairwisecomparison.data.entities.OptionComparisonEntry;
import com.dmko.pairwisecomparison.ui.base.mvp.BasePresenter;
import com.dmko.pairwisecomparison.ui.base.mvp.BaseView;
import com.dmko.pairwisecomparison.ui.screens.comparison.optioncomparisons.filters.OptionComparisonEntryFilter;

import java.util.List;

public interface OptionComparisonsContract {

    interface View extends BaseView {

        void showLoading(boolean isLoading);

        void setOptionComparisons(List<OptionComparisonEntry> optionComparisons);

        void setFilterTypes(List<OptionComparisonEntryFilter> filters);
    }

    interface Presenter extends BasePresenter<View> {

        void start(String comparisonId);

        void setFilter(OptionComparisonEntryFilter filter);

        void updateOptionComparison(OptionComparisonEntry optionComparison);
    }
}