package com.dmko.pairwisecomparison.data.repositories.impl;


import android.util.Log;

import com.dmko.pairwisecomparison.data.dao.ComparisonsDao;
import com.dmko.pairwisecomparison.data.entities.Comparison;
import com.dmko.pairwisecomparison.data.repositories.ComparisonRepository;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.internal.operators.completable.CompletableFromAction;

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
                    Log.i(LOG_DATA, "Retrieving " + Comparison.class.getSimpleName() + "[" + comparisons.size() + "]");
                });
    }

    @Override
    public Flowable<Comparison> getComparisonById(String comparisonId) {
        return comparisonsDao.getComparisonById(comparisonId)
                .doOnNext(comparison -> {
                    Log.i(LOG_DATA, "Retrieving " + comparison);
                });
    }

    @Override
    public Completable insertComparison(Comparison comparison) {
        return new CompletableFromAction(() -> {
            Log.i(LOG_DATA, "Inserting " + comparison);
            comparisonsDao.insertComparison(comparison);
        });
    }

    @Override
    public Completable updateComparison(Comparison comparison) {
        return new CompletableFromAction(() -> {
            Log.i(LOG_DATA, "Updating " + comparison);
            comparisonsDao.updateComparison(comparison);
        });
    }

    @Override
    public Completable deleteComparison(Comparison comparison) {
        return new CompletableFromAction(() -> {
            Log.i(LOG_DATA, "Deleting " + comparison);
            comparisonsDao.deleteComparison(comparison);
        });
    }
}
