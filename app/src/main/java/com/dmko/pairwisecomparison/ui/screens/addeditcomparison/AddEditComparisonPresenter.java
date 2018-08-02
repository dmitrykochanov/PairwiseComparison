package com.dmko.pairwisecomparison.ui.screens.addeditcomparison;

import com.dmko.pairwisecomparison.data.entities.Comparison;
import com.dmko.pairwisecomparison.data.repositories.ComparisonRepository;
import com.dmko.pairwisecomparison.ui.base.mvp.impl.BasePresenterImpl;
import com.dmko.pairwisecomparison.utils.SchedulersFacade;

import timber.log.Timber;

import static com.dmko.pairwisecomparison.utils.LogTags.LOG_APP;

public class AddEditComparisonPresenter extends BasePresenterImpl<AddEditComparisonContract.View> implements AddEditComparisonContract.Presenter {

    private SchedulersFacade schedulers;
    private ComparisonRepository repository;

    private String comparisonId;
    private Comparison comparison;

    public AddEditComparisonPresenter(SchedulersFacade schedulers, ComparisonRepository repository) {
        this.schedulers = schedulers;
        this.repository = repository;
    }

    @Override
    public void setArgs(String comparisonId) {
        this.comparisonId = comparisonId;
    }

    @Override
    public void start() {
        Timber.tag(LOG_APP);
        Timber.i("Starting %s with comparisonId = %s", this.getClass().getSimpleName(), comparisonId);

        getView().showLoading(true);
        if (comparisonId == null) {
            comparison = new Comparison();
            getView().showLoading(false);
        } else {
            addDisposable(repository.getComparisonById(comparisonId)
                    .subscribeOn(schedulers.io())
                    .observeOn(schedulers.ui())
                    .subscribe(comparison -> {
                        Timber.tag(LOG_APP);
                        Timber.i("%s sending to %s, %s", this.getClass().getSimpleName(), getView().getClass().getSimpleName(), comparison);

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
        if (newName.isEmpty()) {
            getView().showEmptyNameError();
        } else {
            comparison.setName(newName);
            if (comparisonId == null) {
                addDisposable(repository.insertComparison(comparison)
                        .subscribeOn(schedulers.io())
                        .subscribe(() -> {
                            if (isViewAttached()) {
                                getView().closeDialog();
                            }
                        }));
            } else {
                addDisposable(repository.updateComparison(comparison)
                        .subscribeOn(schedulers.io())
                        .subscribe(() -> {
                            if (isViewAttached()) {
                                getView().closeDialog();
                            }
                        }));
            }
        }
    }
}
