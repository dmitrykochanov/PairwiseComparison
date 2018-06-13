package com.dmko.pairwisecomparison.ui.screens.pasteoptions;

import com.dmko.pairwisecomparison.data.entities.Option;
import com.dmko.pairwisecomparison.ui.base.mvp.BasePresenter;
import com.dmko.pairwisecomparison.ui.base.mvp.BaseView;

import java.util.List;

public interface PasteOptionsContract {

    interface View extends BaseView {

        void setOptions(List<Option> options);

        void closeDialog();
    }

    interface Presenter extends BasePresenter<PasteOptionsContract.View> {

        void start(String comparisonId, String clipboardContent);

        void updateOption(Option option);

        void deleteOption(Option option);

        void saveOptions();
    }
}
