package com.dmko.pairwisecomparison.data.repositories;


import com.dmko.pairwisecomparison.data.entities.Comparison;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public interface ComparisonRepository {

    Flowable<List<Comparison>> getComparisons();

    Flowable<Comparison> getComparisonById(String comparisonId);

    Completable insertComparison(Comparison comparison);

    Completable updateComparison(Comparison comparison);

    Completable deleteComparison(Comparison comparison);
}
