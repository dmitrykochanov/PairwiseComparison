package com.dmko.pairwisecomparison.ui.screens.comparison.options;

import com.dmko.pairwisecomparison.data.repositories.OptionsRepository;
import com.dmko.pairwisecomparison.ui.base.mvp.impl.BasePresenterImpl;
import com.dmko.pairwisecomparison.ui.screens.comparison.ComparisonContract;
import com.dmko.pairwisecomparison.utils.SchedulersFacade;

import java.util.List;

public class ComparisonPresenter extends BasePresenterImpl<ComparisonContract.View> implements ComparisonContract.Presenter {

    private SchedulersFacade schedulers;
    private OptionsRepository optionsRepository;

    public ComparisonPresenter(SchedulersFacade schedulers, OptionsRepository optionsRepository) {
        this.schedulers = schedulers;
        this.optionsRepository = optionsRepository;
    }

    @Override
    public void onOpenRecompareActivitySelected(String comparisonId, String comparisonName) {
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
}
