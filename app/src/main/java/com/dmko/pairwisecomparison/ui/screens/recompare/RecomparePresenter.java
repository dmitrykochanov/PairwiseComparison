package com.dmko.pairwisecomparison.ui.screens.recompare;

import com.dmko.pairwisecomparison.data.entities.OptionComparisonEntry;
import com.dmko.pairwisecomparison.data.repositories.OptionsRepository;
import com.dmko.pairwisecomparison.data.repositories.SettingsRepository;
import com.dmko.pairwisecomparison.ui.base.mvp.impl.BasePresenterImpl;
import com.dmko.pairwisecomparison.utils.SchedulersFacade;

import java.util.List;

public class RecomparePresenter extends BasePresenterImpl<RecompareContract.View> implements RecompareContract.Presenter {

    private SchedulersFacade schedulers;
    private OptionsRepository optionsRepository;
    private SettingsRepository settingsRepository;

    private int currentPosition = 0;
    private String comparisonId;
    private List<OptionComparisonEntry> optionComparisons;

    public RecomparePresenter(SchedulersFacade schedulers, OptionsRepository optionsRepository, SettingsRepository settingsRepository) {
        this.schedulers = schedulers;
        this.optionsRepository = optionsRepository;
        this.settingsRepository = settingsRepository;
    }

    @Override
    public void setArgs(String comparisonId) {
        this.comparisonId = comparisonId;
    }

    @Override
    public void start() {
        getView().setPromptText(settingsRepository.getPromptText());

        addDisposable(optionsRepository.getOptionComparisonEntriesByComparisonId(comparisonId)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui())
                .subscribe(optionComparisonEntries -> {
                    if (isViewAttached()) {
                        optionComparisons = optionComparisonEntries;
                        changeOptionComparison();
                    }
                }));
    }

    @Override
    public void updateOptionComparison(int newProgress) {
        OptionComparisonEntry entryToUpdate = optionComparisons.get(currentPosition);
        entryToUpdate.setProgress(newProgress);
        addDisposable(optionsRepository.updateOptionComparison(entryToUpdate)
                .subscribeOn(schedulers.io())
                .subscribe());

        if (currentPosition != optionComparisons.size() - 1) {
            currentPosition += 1;
            changeOptionComparison();
        }
    }

    @Override
    public void onPreviousSelected() {
        currentPosition -= 1;
        changeOptionComparison();
    }

    @Override
    public void onNextSelected() {
        currentPosition += 1;
        changeOptionComparison();
    }

    private void changeOptionComparison() {
        getView().setCurrentOptionComparison(optionComparisons.get(currentPosition));
        getView().setNextEnabled(currentPosition != optionComparisons.size() - 1);
        getView().setPreviousEnabled(currentPosition != 0);
        getView().setDoneEnabled(currentPosition == optionComparisons.size() - 1);
        getView().setProgress(String.format("%s/%s", currentPosition + 1, optionComparisons.size()));
    }
}
