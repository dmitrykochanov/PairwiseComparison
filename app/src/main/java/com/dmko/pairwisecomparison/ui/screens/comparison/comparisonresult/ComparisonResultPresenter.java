package com.dmko.pairwisecomparison.ui.screens.comparison.comparisonresult;

import com.dmko.pairwisecomparison.data.entities.Option;
import com.dmko.pairwisecomparison.data.entities.OptionComparisonEntry;
import com.dmko.pairwisecomparison.data.repositories.OptionsRepository;
import com.dmko.pairwisecomparison.ui.base.mvp.impl.BasePresenterImpl;
import com.dmko.pairwisecomparison.utils.SchedulersFacade;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

import static com.dmko.pairwisecomparison.utils.LogTags.LOG_APP;

public class ComparisonResultPresenter extends BasePresenterImpl<ComparisonResultContract.View> implements ComparisonResultContract.Presenter {
    private SchedulersFacade schedulers;
    private OptionsRepository optionsRepository;

    public ComparisonResultPresenter(SchedulersFacade schedulers, OptionsRepository optionsRepository) {
        this.schedulers = schedulers;
        this.optionsRepository = optionsRepository;
    }

    @Override
    public void start(String comparisonId) {
        Timber.tag(LOG_APP);
        Timber.i("Starting %s with comparisonId = %s", this.getClass().getSimpleName(), comparisonId);

        getView().showLoading(true);
        addDisposable(optionsRepository.getOptionComparisonEntriesByComparisonId(comparisonId)
                .subscribeOn(schedulers.io())
                .map(optionComparisonEntries -> {
                    Timber.tag(LOG_APP);
                    Timber.i("Calculating option results for %s[%d]", OptionComparisonEntry.class.getSimpleName(), optionComparisonEntries.size());

                    Map<Option, Integer> results = new HashMap<>();
                    for (OptionComparisonEntry entry : optionComparisonEntries) {
                        if (entry.getProgress() == 0) {
                            continue;
                        }

                        Option key = entry.getProgress() < 0 ? entry.getFirstOption() : entry.getSecondOption();

                        Integer currentProgress = results.get(key);
                        if(currentProgress == null) {
                            currentProgress = 0;
                        }
                        currentProgress += Math.abs(entry.getProgress());
                        results.put(key, currentProgress);
                    }

                    int progressSum = 0;
                    for (Integer progress : results.values()) {
                        progressSum += progress;
                    }

                    if (progressSum != 0) {
                        for (Option option : results.keySet()) {
                            int progressPercentage = Math.round((float) results.get(option) / progressSum * 100);
                            results.put(option, progressPercentage);
                        }
                    }
                    return results;
                })
                .observeOn(schedulers.ui())
                .subscribe(optionResults -> {
                    Timber.tag(LOG_APP);
                    Timber.i("%s sending to %s, %s[%d]", this.getClass().getSimpleName(), getView().getClass().getSimpleName(), Option.class.getSimpleName(), optionResults.size());

                    if (isViewAttached()) {
                        getView().setResults(optionResults);
                        getView().showLoading(false);
                    }
                }));
    }
}
