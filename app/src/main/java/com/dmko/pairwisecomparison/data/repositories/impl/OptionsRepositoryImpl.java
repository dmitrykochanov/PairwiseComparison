package com.dmko.pairwisecomparison.data.repositories.impl;


import com.dmko.pairwisecomparison.data.dao.OptionsDao;
import com.dmko.pairwisecomparison.data.entities.Option;
import com.dmko.pairwisecomparison.data.entities.OptionComparison;
import com.dmko.pairwisecomparison.data.entities.OptionComparisonEntry;
import com.dmko.pairwisecomparison.data.repositories.OptionsRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.internal.operators.completable.CompletableFromAction;
import timber.log.Timber;

import static com.dmko.pairwisecomparison.utils.LogTags.LOG_DATA;

public class OptionsRepositoryImpl implements OptionsRepository {

    private OptionsDao optionsDao;

    public OptionsRepositoryImpl(OptionsDao optionsDao) {
        this.optionsDao = optionsDao;
    }

    @Override
    public Flowable<List<Option>> getOptionsByComparisonId(String comparisonId) {
        return optionsDao.getOptionsByComparisonId(comparisonId)
                .doOnNext(options -> {
                    Timber.tag(LOG_DATA);
                    Timber.i("Retrieving %s[%d]", Option.class.getSimpleName(), options.size());
                });
    }

    @Override
    public Flowable<Option> getOptionById(String optionId) {
        return optionsDao.getOptionById(optionId)
                .doOnNext(option -> {
                    Timber.tag(LOG_DATA);
                    Timber.i("Retrieving %s", option);
                });
    }

    @Override
    public Completable insertOption(Option option) {
        return new CompletableFromAction(() -> {
            List<Option> options = optionsDao.getOptionsByComparisonId(option.getComparisonId()).blockingFirst();
            Timber.tag(LOG_DATA);
            Timber.i("Retrieving %s", option);

            List<OptionComparison> optionComparisons = new ArrayList<>();
            for (Option o : options) {
                OptionComparison optionComparison = new OptionComparison(option.getId(), o.getId(), 0);
                Timber.tag(LOG_DATA);
                Timber.i("Retrieving %s", optionComparison);
                optionComparisons.add(optionComparison);
            }
            Timber.tag(LOG_DATA);
            Timber.i("Inserting %s with %s[%d]", Option.class.getSimpleName(), OptionComparison.class.getSimpleName(), optionComparisons.size());

            optionsDao.insertOptionWithComparisons(option, optionComparisons);
        });
    }

    @Override
    public Completable insertOptions(List<Option> options) {
        return new CompletableFromAction(() -> {
            for (Option option : options) {
                insertOption(option).blockingAwait();
            }
        });
    }

    @Override
    public Completable updateOption(Option option) {
        return new CompletableFromAction(() -> {
            Timber.tag(LOG_DATA);
            Timber.i("Updating %s", option);
            optionsDao.updateOption(option);
        });
    }

    @Override
    public Completable deleteOption(Option option) {
        return new CompletableFromAction(() -> {
            Timber.tag(LOG_DATA);
            Timber.i("Deleting %s", option);
            optionsDao.deleteOption(option);
        });
    }

    @Override
    public Flowable<List<OptionComparisonEntry>> getOptionComparisonEntriesByComparisonId(String comparisonId) {
        return optionsDao.getOptionComparisonEntriesByComparisonId(comparisonId)
                .doOnNext(optionComparisonEntries -> {
                    Timber.tag(LOG_DATA);
                    Timber.i("Retrieving %s[%d]", OptionComparisonEntry.class.getSimpleName(), optionComparisonEntries.size());
                });
    }

    @Override
    public Completable updateOptionComparison(OptionComparisonEntry entry) {
        return new CompletableFromAction(() -> {

            OptionComparison optionComparison = new OptionComparison();
            optionComparison.setId(entry.getId());
            optionComparison.setFirstOptionId(entry.getFirstOption().getId());
            optionComparison.setSecondOptionId(entry.getSecondOption().getId());
            optionComparison.setProgress(entry.getProgress());

            Timber.tag(LOG_DATA);
            Timber.i("Updating %s", optionComparison);

            optionsDao.updateOptionComparison(optionComparison);
        });
    }
}
