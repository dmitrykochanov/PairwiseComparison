package com.dmko.pairwisecomparison.ui.screens.addeditcomparison;

import android.util.Log;

import com.dmko.pairwisecomparison.data.entities.Comparison;
import com.dmko.pairwisecomparison.data.repositories.ComparisonRepository;
import com.dmko.pairwisecomparison.ui.base.mvp.impl.BasePresenterImpl;
import com.dmko.pairwisecomparison.utils.SchedulersFacade;

import static com.dmko.pairwisecomparison.utils.LogTags.LOG_APP;

public class AddEditComparisonPresenter extends BasePresenterImpl<AddEditComparisonContract.View> implements AddEditComparisonContract.Presenter {
    private SchedulersFacade schedulers;
    private ComparisonRepository repository;
    private Comparison comparison;
    private boolean isNew;

    public AddEditComparisonPresenter(SchedulersFacade schedulers, ComparisonRepository repository) {
        this.schedulers = schedulers;
        this.repository = repository;
    }

    @Override
    public void start(String comparisonId) {
        Log.i(LOG_APP, "Comparison id = " + comparisonId);

        getView().showLoading(true);
        if (comparisonId == null) {
            isNew = true;
            comparison = new Comparison();
            getView().setComparison(comparison);
            getView().showLoading(false);
        } else {
            addDisposable(repository.getComparisonById(comparisonId)
                    .subscribeOn(schedulers.io())
                    .observeOn(schedulers.ui())
                    .subscribe(comparison -> {
                        Log.i(LOG_APP, "Sending comparison to the view " + comparison);
                        if (isViewAttached()) {
                            this.comparison = comparison;
                            getView().setComparison(comparison);
                            getView().showLoading(false);
                        }
                    }));
        }
    }

    @Override
    public void saveComparison(String newName) {
        Log.i(LOG_APP, "New comparison name = [" + newName + "]");
        comparison.setName(newName);
        if (isNew) {
            addDisposable(repository.insertComparison(comparison)
                    .subscribeOn(schedulers.io())
                    .subscribe());
        } else {
            addDisposable(repository.updateComparison(comparison)
                    .subscribeOn(schedulers.io())
                    .subscribe());
        }
    }
}
