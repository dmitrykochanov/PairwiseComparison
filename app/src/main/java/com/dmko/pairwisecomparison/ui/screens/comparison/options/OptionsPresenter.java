package com.dmko.pairwisecomparison.ui.screens.comparison.options;

import android.util.Log;

import com.dmko.pairwisecomparison.data.entities.Option;
import com.dmko.pairwisecomparison.data.repositories.OptionsRepository;
import com.dmko.pairwisecomparison.ui.base.mvp.impl.BasePresenterImpl;
import com.dmko.pairwisecomparison.utils.SchedulersFacade;

import java.util.Collections;

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
        Log.i(LOG_APP, "Starting " + OptionsPresenter.class.getSimpleName());
        Log.i(LOG_APP, "Comparison id = " + comparisonId);

        getView().showLoading(true);
        this.comparisonId = comparisonId;
        addDisposable(optionsRepository.getOptionsByComparisonId(comparisonId)
                .doOnNext(Collections::sort)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui())
                .subscribe(options -> {
                    Log.i(LOG_APP, "Sending " + Option.class.getSimpleName() + "[" + options.size() + "] to the view");
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
}
