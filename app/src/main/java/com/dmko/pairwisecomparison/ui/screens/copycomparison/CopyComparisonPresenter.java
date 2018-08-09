package com.dmko.pairwisecomparison.ui.screens.copycomparison;

import com.dmko.pairwisecomparison.data.repositories.ComparisonRepository;
import com.dmko.pairwisecomparison.ui.base.mvp.impl.BasePresenterImpl;
import com.dmko.pairwisecomparison.utils.SchedulersFacade;

public class CopyComparisonPresenter extends BasePresenterImpl<CopyComparisonContract.View> implements CopyComparisonContract.Presenter {

    private SchedulersFacade schedulers;
    private ComparisonRepository comparisonRepository;

    private String comparisonId;

    public CopyComparisonPresenter(SchedulersFacade schedulers, ComparisonRepository comparisonRepository) {
        this.schedulers = schedulers;
        this.comparisonRepository = comparisonRepository;
    }

    @Override
    public void setArgs(String comparisonId) {
        this.comparisonId = comparisonId;
    }

    @Override
    public void start() {
        getView().showLoading(true);
        addDisposable(comparisonRepository.getComparisonById(comparisonId)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui())
                .subscribe(comparison -> {
                    if (isViewAttached()) {
                        getView().showLoading(false);
                        getView().setComparison(comparison);
                    }
                }));
    }

    @Override
    public void onButtonOkClicked(String newName, boolean copyOptionComparisons) {
        getView().showLoading(true);
        addDisposable(comparisonRepository.copyComparison(comparisonId, newName, copyOptionComparisons)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui())
                .subscribe(() -> {
                    if (isViewAttached()) {
                        getView().showLoading(false);
                        getView().closeDialog();
                    }
                }));
    }
}
