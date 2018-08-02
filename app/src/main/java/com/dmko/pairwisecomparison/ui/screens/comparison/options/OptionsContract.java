package com.dmko.pairwisecomparison.ui.screens.comparison.options;

import com.dmko.pairwisecomparison.data.entities.Option;
import com.dmko.pairwisecomparison.ui.base.mvp.BasePresenter;
import com.dmko.pairwisecomparison.ui.base.mvp.BaseView;

import java.util.List;

public interface OptionsContract {

    interface View extends BaseView {

        void showLoading(boolean isLoading);

        void setOptions(List<Option> options);

        void setEmptyOptions();

        void showOptionDialog(String comparisonId, String optionId);
    }

    interface Presenter extends BasePresenter<View> {

        void setArgs(String comparisonId);

        void start();

        void updateOptionSelected(Option option);

        void deleteOptionSelected(Option option);

        void addOption(String optionName);
    }
}