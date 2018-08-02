package com.dmko.pairwisecomparison.ui.screens.comparisons;

import com.dmko.pairwisecomparison.data.entities.Comparison;
import com.dmko.pairwisecomparison.ui.base.mvp.BasePresenter;
import com.dmko.pairwisecomparison.ui.base.mvp.BaseView;

import java.util.List;

public interface ComparisonsContract {

    interface View extends BaseView {

        void showLoading(boolean isLoading);

        void setComparisons(List<Comparison> comparisons);

        void setEmptyComparisons();

        void showAddEditComparisonDialog(String comparisonId);

        void openComparison(Comparison comparison);
    }

    interface Presenter extends BasePresenter<View> {

        void start();

        void openComparisonSelected(Comparison comparison);

        void addComparisonSelected();

        void updateComparisonSelected(Comparison comparison);

        void deleteComparisonSelected(Comparison comparison);
    }
}