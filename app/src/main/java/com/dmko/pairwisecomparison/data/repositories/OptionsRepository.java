package com.dmko.pairwisecomparison.data.repositories;


import com.dmko.pairwisecomparison.data.entities.Option;
import com.dmko.pairwisecomparison.data.entities.OptionComparisonEntry;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public interface OptionsRepository {

    Flowable<List<Option>> getOptionsByComparisonId(String comparisonId);

    Flowable<Option> getOptionById(String optionId);

    Completable insertOption(Option option);

    Completable updateOption(Option option);

    Completable deleteOption(Option option);

    Flowable<List<OptionComparisonEntry>> getOptionComparisonEntriesByComparisonId(String comparisonId);

    Completable updateOptionComparison(OptionComparisonEntry optionComparison);
}
