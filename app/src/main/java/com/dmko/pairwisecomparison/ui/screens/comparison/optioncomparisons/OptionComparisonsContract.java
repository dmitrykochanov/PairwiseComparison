package com.dmko.pairwisecomparison.ui.screens.comparison.optioncomparisons;

import android.content.res.Resources;

import com.dmko.pairwisecomparison.data.entities.OptionComparisonEntry;
import com.dmko.pairwisecomparison.ui.base.mvp.BasePresenter;
import com.dmko.pairwisecomparison.ui.base.mvp.BaseView;
import com.dmko.pairwisecomparison.ui.screens.comparison.optioncomparisons.filters.OptionComparisonEntryFilter;

import java.util.List;

public interface OptionComparisonsContract {

    interface View extends BaseView {

        void showLoading(boolean isLoading);

        void setEmptyOptionComparisons();

        void setOptionComparisons(List<OptionComparisonEntry> optionComparisons);

        void setFilterTypes(List<OptionComparisonEntryFilter> filters);
    }

    interface Presenter extends BasePresenter<View> {

        void setArgs(String comparisonId, Resources resources);

        void start();

        void setFilter(OptionComparisonEntryFilter filter);

        void updateOptionComparison(OptionComparisonEntry optionComparison);
    }
}