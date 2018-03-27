package com.dmko.pairwisecomparison.ui.screens.addeditoption;

import android.util.Log;

import com.dmko.pairwisecomparison.data.entities.Option;
import com.dmko.pairwisecomparison.data.repositories.OptionsRepository;
import com.dmko.pairwisecomparison.ui.base.mvp.impl.BasePresenterImpl;
import com.dmko.pairwisecomparison.utils.SchedulersFacade;

import static com.dmko.pairwisecomparison.utils.LogTags.LOG_APP;

public class AddEditOptionPresenter extends BasePresenterImpl<AddEditOptionContract.View> implements AddEditOptionContract.Presenter {
    private SchedulersFacade schedulers;
    private OptionsRepository optionsRepository;
    private Option option;
    private boolean isNew;

    public AddEditOptionPresenter(SchedulersFacade schedulers, OptionsRepository optionsRepository) {
        this.schedulers = schedulers;
        this.optionsRepository = optionsRepository;
    }

    @Override
    public void start(String comparisonId, String optionId) {
        Log.i(LOG_APP, "Starting " + AddEditOptionPresenter.class.getSimpleName());
        Log.i(LOG_APP, "Comparison id = " + comparisonId + ", option id = " + optionId);

        getView().showLoading(true);
        if (optionId == null) {
            isNew = true;
            option = new Option();
            option.setComparisonId(comparisonId);
            getView().setOption(option);
            getView().showLoading(false);
        } else {
            addDisposable(optionsRepository.getOptionById(optionId)
                    .subscribeOn(schedulers.io())
                    .observeOn(schedulers.ui())
                    .subscribe(option -> {
                        if (isViewAttached()) {
                            this.option = option;
                            getView().setOption(option);
                            getView().showLoading(false);
                        }
                    }));
        }
    }

    @Override
    public void saveOption(String optionName) {
        Log.i(LOG_APP, "New option name = [" + optionName + "]");
        option.setName(optionName);
        if (isNew) {
            addDisposable(optionsRepository.insertOption(option)
                    .subscribeOn(schedulers.io())
                    .subscribe());
        } else {
            addDisposable(optionsRepository.updateOption(option)
                    .subscribeOn(schedulers.io())
                    .subscribe());
        }
    }
}
