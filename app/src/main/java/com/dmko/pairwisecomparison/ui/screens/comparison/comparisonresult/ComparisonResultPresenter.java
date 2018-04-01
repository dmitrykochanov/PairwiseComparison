package com.dmko.pairwisecomparison.ui.screens.comparison.comparisonresult;

import com.dmko.pairwisecomparison.data.entities.Option;
import com.dmko.pairwisecomparison.data.entities.OptionCompareResultEntry;
import com.dmko.pairwisecomparison.data.entities.OptionComparisonEntry;
import com.dmko.pairwisecomparison.data.repositories.OptionsRepository;
import com.dmko.pairwisecomparison.ui.base.mvp.impl.BasePresenterImpl;
import com.dmko.pairwisecomparison.utils.SchedulersFacade;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

import static com.dmko.pairwisecomparison.utils.LogTags.LOG_APP;

public class ComparisonResultPresenter extends BasePresenterImpl<ComparisonResultContract.View> implements ComparisonResultContract.Presenter {
    private SchedulersFacade schedulers;
    private OptionsRepository optionsRepository;
    private PublishSubject<Integer> chartTypeSubject;

    public ComparisonResultPresenter(SchedulersFacade schedulers, OptionsRepository optionsRepository) {
        this.schedulers = schedulers;
        this.optionsRepository = optionsRepository;
        chartTypeSubject = PublishSubject.create();
    }

    @Override
    public void start(String comparisonId) {
        Timber.tag(LOG_APP);
        Timber.i("Starting %s with comparisonId = %s", this.getClass().getSimpleName(), comparisonId);

        getView().showLoading(true);

        Flowable<Map<Option, Integer>> calculatedResultsFlowable = optionsRepository.getOptionComparisonEntriesByComparisonId(comparisonId)
                .subscribeOn(schedulers.io())
                .map(optionComparisonEntries -> {
                    Timber.tag(LOG_APP);
                    Timber.i("Calculating option results for %s[%d]", OptionComparisonEntry.class.getSimpleName(), optionComparisonEntries.size());

                    Map<Option, Integer> results = new HashMap<>();
                    for (OptionComparisonEntry entry : optionComparisonEntries) {
                        if (!results.containsKey(entry.getFirstOption())) {
                            results.put(entry.getFirstOption(), 0);
                        }
                        if (!results.containsKey(entry.getSecondOption())) {
                            results.put(entry.getSecondOption(), 0);
                        }

                        Option key = entry.getProgress() < 0 ? entry.getFirstOption() : entry.getSecondOption();

                        Integer currentProgress = results.get(key);
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
                    } else {
                        results.clear();
                    }
                    return results;
                });

        Flowable<Integer> chartTypeFlowable = chartTypeSubject.toFlowable(BackpressureStrategy.BUFFER);

        addDisposable(Flowable.combineLatest(calculatedResultsFlowable, chartTypeFlowable, OptionCompareResultEntry::new)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui())
                .subscribe(results -> {
                    getView().showLoading(false);
                    getView().setResults(results.getResults(), results.getChartType());
                }));
    }

    @Override
    public void setChartType(int chartType) {
        chartTypeSubject.onNext(chartType);
    }

    @Override
    public void saveChartSelected(int chartType) {
        getView().saveChart(chartType);
    }
}
