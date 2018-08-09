package com.dmko.pairwisecomparison.ui.screens.comparisons;

import com.dmko.pairwisecomparison.data.entities.Comparison;
import com.dmko.pairwisecomparison.data.repositories.ComparisonRepository;
import com.dmko.pairwisecomparison.ui.base.mvp.impl.BasePresenterImpl;
import com.dmko.pairwisecomparison.utils.SchedulersFacade;

import java.util.Collections;

import timber.log.Timber;

import static com.dmko.pairwisecomparison.utils.LogTags.LOG_APP;

public class ComparisonsPresenter extends BasePresenterImpl<ComparisonsContract.View> implements ComparisonsContract.Presenter {

    private SchedulersFacade schedulers;
    private ComparisonRepository repository;

    public ComparisonsPresenter(SchedulersFacade schedulers, ComparisonRepository repository) {
        this.schedulers = schedulers;
        this.repository = repository;
    }

    @Override
    public void start() {
        Timber.tag(LOG_APP);
        Timber.i("Starting %s", this.getClass().getSimpleName());

        getView().showLoading(true);

        addDisposable(repository.getComparisons()
                .doOnNext(Collections::sort)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui())
                .subscribe(comparisons -> {
                    Timber.tag(LOG_APP);
                    Timber.i("%s sending to %s, %s[%d]", this.getClass().getSimpleName(), getView().getClass().getSimpleName(), Comparison.class.getSimpleName(), comparisons.size());

                    if (isViewAttached()) {
                        if (comparisons.isEmpty()) {
                            getView().setEmptyComparisons();
                        } else {
                            getView().setComparisons(comparisons);
                        }
                        getView().showLoading(false);
                    }
                }));
    }

    @Override
    public void openComparisonSelected(Comparison comparison) {
        getView().openComparison(comparison);
    }

    @Override
    public void addComparisonSelected() {
        getView().showAddEditComparisonDialog(null);
    }

    @Override
    public void updateComparisonSelected(Comparison comparison) {
        getView().showAddEditComparisonDialog(comparison.getId());
    }

    @Override
    public void deleteComparisonSelected(Comparison comparison) {
        addDisposable(repository.deleteComparison(comparison)
                .subscribeOn(schedulers.io())
                .subscribe());
    }

    @Override
    public void copyComparisonSelected(Comparison comparison) {
        getView().openCopyComparisonDialog(comparison.getId());
    }
}
