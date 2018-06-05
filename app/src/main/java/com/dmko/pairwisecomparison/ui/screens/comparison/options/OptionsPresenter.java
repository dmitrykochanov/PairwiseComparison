package com.dmko.pairwisecomparison.ui.screens.comparison.options;

import com.dmko.pairwisecomparison.data.entities.Option;
import com.dmko.pairwisecomparison.data.repositories.OptionsRepository;
import com.dmko.pairwisecomparison.ui.base.mvp.impl.BasePresenterImpl;
import com.dmko.pairwisecomparison.utils.SchedulersFacade;

import java.util.Collections;

import timber.log.Timber;

import static com.dmko.pairwisecomparison.utils.LogTags.LOG_APP;

public class OptionsPresenter extends BasePresenterImpl<OptionsContract.View> implements OptionsContract.Presenter {
    private SchedulersFacade schedulers;
    private OptionsRepository optionsRepository;
    private String comparisonId;

    public OptionsPresenter(SchedulersFacade schedulers, OptionsRepository optionsRepository) {
        this.schedulers = schedulers;
        this.optionsRepository = optionsRepository;
    }

    @Override
    public void start(String comparisonId) {
        Timber.tag(LOG_APP);
        Timber.i("Starting %s with comparisonId = %s", this.getClass().getSimpleName(), comparisonId);

        getView().showLoading(true);
        this.comparisonId = comparisonId;
        addDisposable(optionsRepository.getOptionsByComparisonId(comparisonId)
                .doOnNext(Collections::sort)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui())
                .subscribe(options -> {
                    Timber.tag(LOG_APP);
                    Timber.i("%s sending to %s, %s[%d]", this.getClass().getSimpleName(), getView().getClass().getSimpleName(), Option.class.getSimpleName(), options.size());

                    if (isViewAttached()) {
                        getView().setOptions(options);
                        getView().showLoading(false);
                    }
                }));
    }

    @Override
    public void addOptionSelected() {
        if (isViewAttached()) {
            getView().showOptionDialog(comparisonId, null);
        }
    }

    @Override
    public void updateOptionSelected(Option option) {
        if (isViewAttached()) {
            getView().showOptionDialog(comparisonId, option.getId());
        }
    }

    @Override
    public void deleteOptionSelected(Option option) {
        addDisposable(optionsRepository.deleteOption(option)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui())
                .subscribe());
    }

    @Override
    public void addOption(String optionName) {
        Option option = new Option(comparisonId, optionName);
        addDisposable(optionsRepository.insertOption(option)
                .subscribeOn(schedulers.io())
                .subscribe());
    }
}
