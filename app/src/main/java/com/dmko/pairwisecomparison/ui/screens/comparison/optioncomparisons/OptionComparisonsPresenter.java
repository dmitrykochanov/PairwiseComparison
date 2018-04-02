package com.dmko.pairwisecomparison.ui.screens.comparison.optioncomparisons;

import com.dmko.pairwisecomparison.data.entities.OptionComparison;
import com.dmko.pairwisecomparison.data.entities.OptionComparisonEntry;
import com.dmko.pairwisecomparison.data.repositories.OptionsRepository;
import com.dmko.pairwisecomparison.ui.base.mvp.impl.BasePresenterImpl;
import com.dmko.pairwisecomparison.utils.SchedulersFacade;

import java.util.Collections;
import java.util.List;

import timber.log.Timber;

import static com.dmko.pairwisecomparison.utils.LogTags.LOG_APP;

public class OptionComparisonsPresenter extends BasePresenterImpl<OptionComparisonsContract.View> implements OptionComparisonsContract.Presenter {
    private SchedulersFacade schedulers;
    private OptionsRepository optionsRepository;
    private String comparisonId;

    public OptionComparisonsPresenter(SchedulersFacade schedulers, OptionsRepository optionsRepository) {
        this.schedulers = schedulers;
        this.optionsRepository = optionsRepository;
    }

    @Override
    public void start(String comparisonId) {
        Timber.tag(LOG_APP);
        Timber.i("Starting %s with comparisonId = %s", this.getClass().getSimpleName(), comparisonId);

        this.comparisonId = comparisonId;
        getView().showLoading(true);
        addDisposable(optionsRepository.getOptionComparisonEntriesByComparisonId(comparisonId)
                .doOnNext(Collections::sort)
                .distinctUntilChanged()
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui())
                .subscribe(optionComparisons -> {
                    Timber.tag(LOG_APP);
                    Timber.i("%s sending to %s, %s[%d]", this.getClass().getSimpleName(), getView().getClass().getSimpleName(), OptionComparison.class.getSimpleName(), optionComparisons.size());

                    if (isViewAttached()) {
                        getView().setOptionComparisons(optionComparisons);
                        getView().showLoading(false);
                    }
                }));
    }

    @Override
    public void updateOptionComparison(OptionComparisonEntry optionComparison) {
        addDisposable(optionsRepository.updateOptionComparison(optionComparison)
                .subscribeOn(schedulers.io())
                .subscribe());
    }

    @Override
    public void onAddOptionSelected() {
        getView().showOptionDialog(comparisonId);
    }
}
