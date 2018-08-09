package com.dmko.pairwisecomparison.ui.screens.comparison;

import com.dmko.pairwisecomparison.data.repositories.ComparisonRepository;
import com.dmko.pairwisecomparison.data.repositories.OptionsRepository;
import com.dmko.pairwisecomparison.ui.base.mvp.impl.BasePresenterImpl;
import com.dmko.pairwisecomparison.utils.SchedulersFacade;

import java.util.List;

public class ComparisonPresenter extends BasePresenterImpl<ComparisonContract.View> implements ComparisonContract.Presenter {

    private SchedulersFacade schedulers;
    private OptionsRepository optionsRepository;
    private ComparisonRepository comparisonRepository;

    private String comparisonId;

    public ComparisonPresenter(SchedulersFacade schedulers, OptionsRepository optionsRepository, ComparisonRepository comparisonRepository) {
        this.schedulers = schedulers;
        this.optionsRepository = optionsRepository;
        this.comparisonRepository = comparisonRepository;
    }

    @Override
    public void setArgs(String comparisonId) {
        this.comparisonId = comparisonId;
    }

    @Override
    public void onOpenRecompareActivitySelected(String comparisonName) {
        addDisposable(optionsRepository.getOptionComparisonEntriesByComparisonId(comparisonId)
                .firstOrError()
                .map(List::size)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui())
                .subscribe(size -> {
                    if (isViewAttached()) {
                        if (size == 0) {
                            getView().showNothingToCompareDialog();
                        } else {
                            getView().openRecompareActivity(comparisonId, comparisonName);
                        }
                    }
                }));
    }

    @Override
    public void clearOptionComparisons() {
        addDisposable(comparisonRepository.clearOptionComparisons(comparisonId)
                .subscribeOn(schedulers.io())
                .subscribe());
    }
}
