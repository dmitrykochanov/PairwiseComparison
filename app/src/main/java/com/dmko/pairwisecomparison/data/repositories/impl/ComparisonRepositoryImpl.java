package com.dmko.pairwisecomparison.data.repositories.impl;


import com.dmko.pairwisecomparison.data.dao.ComparisonsDao;
import com.dmko.pairwisecomparison.data.dao.OptionsDao;
import com.dmko.pairwisecomparison.data.entities.Comparison;
import com.dmko.pairwisecomparison.data.entities.Option;
import com.dmko.pairwisecomparison.data.entities.OptionComparison;
import com.dmko.pairwisecomparison.data.repositories.ComparisonRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.internal.operators.completable.CompletableFromAction;
import timber.log.Timber;

import static com.dmko.pairwisecomparison.utils.LogTags.LOG_DATA;

public class ComparisonRepositoryImpl implements ComparisonRepository {

    private ComparisonsDao comparisonsDao;
    private OptionsDao optionsDao;

    public ComparisonRepositoryImpl(ComparisonsDao comparisonsDao, OptionsDao optionsDao) {
        this.comparisonsDao = comparisonsDao;
        this.optionsDao = optionsDao;
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

    @Override
    public Completable copyComparison(String comparisonId, String newName, boolean copyOptionComparisons) {
        return new CompletableFromAction(() -> {
            //Copy comparison
            Comparison newComparison = new Comparison(newName);
            comparisonsDao.insertComparison(newComparison);

            //Copy options and remember the relation between old and new options ids
            List<Option> oldOptions = optionsDao.getOptionsByComparisonId(comparisonId).blockingFirst();
            List<Option> newOptions = new ArrayList<>(oldOptions.size());
            Map<String, String> relation = new HashMap<>();

            for (Option oldOption : oldOptions) {
                Option newOption = new Option(newComparison.getId(), oldOption.getName());
                relation.put(oldOption.getId(), newOption.getId());
                newOptions.add(newOption);
            }
            optionsDao.insertOptions(newOptions);

            //Copy option comparisons
            List<OptionComparison> oldOptionComparisons = optionsDao.getOptionComparisonsByComparisonId(comparisonId).blockingFirst();
            List<OptionComparison> newOptionComparisons = new ArrayList<>(oldOptionComparisons.size());

            for (OptionComparison oldOptionComparison : oldOptionComparisons) {
                String firstOptionId = relation.get(oldOptionComparison.getFirstOptionId());
                String secondOptionId = relation.get(oldOptionComparison.getSecondOptionId());
                Integer progress = copyOptionComparisons ? oldOptionComparison.getProgress() : 0;
                newOptionComparisons.add(new OptionComparison(firstOptionId, secondOptionId, progress));
            }
            optionsDao.insertOptionComparisons(newOptionComparisons);
        });
    }
}
