package com.dmko.pairwisecomparison.ui.screens.addeditoption;

import com.dmko.pairwisecomparison.data.entities.Option;
import com.dmko.pairwisecomparison.data.repositories.OptionsRepository;
import com.dmko.pairwisecomparison.ui.base.mvp.impl.BasePresenterImpl;
import com.dmko.pairwisecomparison.utils.SchedulersFacade;

import timber.log.Timber;

import static com.dmko.pairwisecomparison.utils.LogTags.LOG_APP;

public class AddEditOptionPresenter extends BasePresenterImpl<AddEditOptionContract.View> implements AddEditOptionContract.Presenter {

    private SchedulersFacade schedulers;
    private OptionsRepository optionsRepository;

    private String comparisonId;
    private String optionId;
    private Option option;

    public AddEditOptionPresenter(SchedulersFacade schedulers, OptionsRepository optionsRepository) {
        this.schedulers = schedulers;
        this.optionsRepository = optionsRepository;
    }

    @Override
    public void setArgs(String comparisonId, String optionId) {
        this.comparisonId = comparisonId;
        this.optionId = optionId;
    }

    @Override
    public void start() {
        Timber.tag(LOG_APP);
        Timber.i("Starting %s with optionId = %s, comparisonId = %s", this.getClass().getSimpleName(), optionId, comparisonId);

        getView().showLoading(true);
        if (optionId == null) {
            option = new Option();
            option.setComparisonId(comparisonId);
            getView().showLoading(false);
        } else {
            addDisposable(optionsRepository.getOptionById(optionId)
                    .subscribeOn(schedulers.io())
                    .observeOn(schedulers.ui())
                    .subscribe(option -> {
                        if (isViewAttached()) {
                            Timber.tag(LOG_APP);
                            Timber.i("%s sending to %s, %s", this.getClass().getSimpleName(), getView().getClass().getSimpleName(), option);

                            this.option = option;
                            getView().setOption(option);
                            getView().showLoading(false);
                        }
                    }));
        }
    }

    @Override
    public void saveOption(String optionName) {
        if (optionName.isEmpty()) {
            getView().showEmptyNameError();
        } else {
            option.setName(optionName);
            if (optionId == null) {
                addDisposable(optionsRepository.insertOption(option)
                        .subscribeOn(schedulers.io())
                        .subscribe(() -> {
                            if (isViewAttached()) {
                                getView().closeDialog();
                            }
                        }));
            } else {
                addDisposable(optionsRepository.updateOption(option)
                        .subscribeOn(schedulers.io())
                        .subscribe(() -> {
                            if (isViewAttached()) {
                                getView().closeDialog();
                            }
                        }));
            }
        }
    }
}
