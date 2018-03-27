package com.dmko.pairwisecomparison.ui.screens.comparisons;

import android.util.Log;

import com.dmko.pairwisecomparison.data.entities.Comparison;
import com.dmko.pairwisecomparison.data.repositories.ComparisonRepository;
import com.dmko.pairwisecomparison.ui.base.mvp.impl.BasePresenterImpl;
import com.dmko.pairwisecomparison.utils.SchedulersFacade;

import java.util.Collections;

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
        Log.i(LOG_APP, "Starting " + ComparisonsPresenter.class.getSimpleName());
        getView().showLoading(true);

        addDisposable(repository.getComparisons()
                .doOnNext(Collections::sort)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui())
                .subscribe(comparisons -> {
                    Log.i(LOG_APP, "Sending " + Comparison.class.getSimpleName() + "[" + comparisons.size() + "] to the view");
                    if (isViewAttached()) {
                        getView().setComparisons(comparisons);
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
        getView().showComparisonDialog(null);
    }

    @Override
    public void updateComparisonSelected(Comparison comparison) {
        getView().showComparisonDialog(comparison.getId());
    }

    @Override
    public void deleteComparisonSelected(Comparison comparison) {
        addDisposable(repository.deleteComparison(comparison)
                .subscribeOn(schedulers.io())
                .subscribe());
    }
}
