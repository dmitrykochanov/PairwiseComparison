package com.dmko.pairwisecomparison.ui.screens.comparison.comparisonresult;

import android.util.Log;

import com.dmko.pairwisecomparison.data.entities.Option;
import com.dmko.pairwisecomparison.data.entities.OptionComparisonEntry;
import com.dmko.pairwisecomparison.data.repositories.OptionsRepository;
import com.dmko.pairwisecomparison.ui.base.mvp.impl.BasePresenterImpl;
import com.dmko.pairwisecomparison.utils.SchedulersFacade;

import java.util.HashMap;
import java.util.Map;

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
        Log.i(LOG_APP, "Starting " + ComparisonResultPresenter.class.getSimpleName());
        Log.i(LOG_APP, "Comparison id = " + comparisonId);

        getView().showLoading(true);
        addDisposable(optionsRepository.getOptionComparisonEntriesByComparisonId(comparisonId)
                .subscribeOn(schedulers.io())
                .map(optionComparisonEntries -> {
                    Log.i(LOG_APP, "Calculating option results for " + optionComparisonEntries.size() + " " + OptionComparisonEntry.class.getSimpleName());
                    Map<Option, Integer> results = new HashMap<>();
                    for (OptionComparisonEntry entry : optionComparisonEntries) {
                        Log.i(LOG_APP, " " + entry);
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
                    Log.i(LOG_APP, "Sending " + optionResults.size() + " results to the view");
                    if (isViewAttached()) {
                        getView().setResults(optionResults);
                        getView().showLoading(false);
                    }
                }));
    }
}
