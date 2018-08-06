package com.dmko.pairwisecomparison.ui.screens.comparison.comparisonresult;

import com.dmko.pairwisecomparison.data.entities.Option;
import com.dmko.pairwisecomparison.data.repositories.OptionsRepository;
import com.dmko.pairwisecomparison.data.repositories.SettingsRepository;
import com.dmko.pairwisecomparison.interactors.ComparisonResultCalculator;
import com.dmko.pairwisecomparison.ui.base.mvp.impl.BasePresenterImpl;
import com.dmko.pairwisecomparison.utils.SchedulersFacade;

import java.util.List;
import java.util.Map;

import timber.log.Timber;

import static com.dmko.pairwisecomparison.utils.LogTags.LOG_APP;

public class ComparisonResultPresenter extends BasePresenterImpl<ComparisonResultContract.View> implements ComparisonResultContract.Presenter {

    private SchedulersFacade schedulers;
    private OptionsRepository optionsRepository;
    private SettingsRepository settingsRepository;
    private ComparisonResultCalculator comparisonResultCalculator;

    private String comparisonId;
    private String comparisonName;
    private Map<Option, Integer> results;
    private boolean showDefault = true;

    public ComparisonResultPresenter(SchedulersFacade schedulers, OptionsRepository optionsRepository,
                                     SettingsRepository settingsRepository, ComparisonResultCalculator comparisonResultCalculator) {
        this.schedulers = schedulers;
        this.optionsRepository = optionsRepository;
        this.settingsRepository = settingsRepository;
        this.comparisonResultCalculator = comparisonResultCalculator;
    }

    @Override
    public void setArgs(String comparisonId, String comparisonName) {
        this.comparisonId = comparisonId;
        this.comparisonName = comparisonName;
    }

    @Override
    public void start() {
        Timber.tag(LOG_APP);
        Timber.i("Starting %s with comparisonId = %s", this.getClass().getSimpleName(), comparisonId);

        getView().showLoading(true);

        addDisposable(optionsRepository.getOptionComparisonEntriesByComparisonId(comparisonId)
                .map(comparisonResultCalculator::calculateComparisonResult)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui())
                .subscribe(results -> {
                    if (isViewAttached()) {
                        this.results = results;
                        sendResultsToView(results);
                    }
                }));
    }

    @Override
    public void onChartTypeChanged() {
        sendResultsToView(results);
    }

    @Override
    public void saveChartSelected(int chartType) {
        getView().saveChart(chartType, comparisonName);
    }

    @Override
    public String convertResultsToText(List<Map.Entry<Option, Integer>> results) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<Option, Integer> entry : results) {
            String optionResult = String.format("%s(%s%%)\n", entry.getKey().getName(), entry.getValue());
            stringBuilder.append(optionResult);
        }
        return stringBuilder.toString();
    }

    private void sendResultsToView(Map<Option, Integer> results) {
        if (isViewAttached()) {
            int defaultChart = settingsRepository.getDefaultChart();
            if (showDefault) {
                showDefault = false;
            } else {
                defaultChart = -1;
            }
            getView().setResults(results, defaultChart, settingsRepository.getChartColors(), settingsRepository.showPercentage());
            getView().showLoading(false);
        }
    }
}
