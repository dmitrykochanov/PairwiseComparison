package com.dmko.pairwisecomparison.data.repositories.impl;


import com.dmko.pairwisecomparison.data.dao.ComparisonsDao;
import com.dmko.pairwisecomparison.data.entities.Comparison;
import com.dmko.pairwisecomparison.data.repositories.ComparisonRepository;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.internal.operators.completable.CompletableFromAction;
import timber.log.Timber;

import static com.dmko.pairwisecomparison.utils.LogTags.LOG_DATA;

public class ComparisonRepositoryImpl implements ComparisonRepository {
    private ComparisonsDao comparisonsDao;

    public ComparisonRepositoryImpl(ComparisonsDao comparisonsDao) {
        this.comparisonsDao = comparisonsDao;
    }

    @Override
    public Flowable<List<Comparison>> getComparisons() {
        return comparisonsDao.getComparisons()
                .doOnNext(comparisons -> {
                    Timber.tag(LOG_DATA);
                    Timber.i("Retrieving %s[%d]", Comparison.class.getSimpleName(), comparisons.size());
                });
    }

    @Override
    public Flowable<Comparison> getComparisonById(String comparisonId) {
        return comparisonsDao.getComparisonById(comparisonId)
                .doOnNext(comparison -> {
                    Timber.tag(LOG_DATA);
                    Timber.i("Retrieving %s", comparison.toString());
                });
    }

    @Override
    public Completable insertComparison(Comparison comparison) {
        return new CompletableFromAction(() -> {
            Timber.tag(LOG_DATA);
            Timber.i("Inserting %s", comparison.toString());
            comparisonsDao.insertComparison(comparison);
        });
    }

    @Override
    public Completable updateComparison(Comparison comparison) {
        return new CompletableFromAction(() -> {
            Timber.tag(LOG_DATA);
            Timber.i("Updating %s", comparison.toString());
            comparisonsDao.updateComparison(comparison);
        });
    }

    @Override
    public Completable deleteComparison(Comparison comparison) {
        return new CompletableFromAction(() -> {
            Timber.tag(LOG_DATA);
            Timber.i("Deleting %s", comparison.toString());
            comparisonsDao.deleteComparison(comparison);
        });
    }
}
