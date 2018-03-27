package com.dmko.pairwisecomparison.data.repositories.impl;


import android.util.Log;

import com.dmko.pairwisecomparison.data.dao.ComparisonsDao;
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

import static com.dmko.pairwisecomparison.utils.LogTags.LOG_DATA;

public class OptionsRepositoryImpl implements OptionsRepository {
    private ComparisonsDao comparisonsDao;
    private OptionsDao optionsDao;

    public OptionsRepositoryImpl(ComparisonsDao comparisonsDao, OptionsDao optionsDao) {
        this.comparisonsDao = comparisonsDao;
        this.optionsDao = optionsDao;
    }

    @Override
    public Flowable<List<Option>> getOptionsByComparisonId(String comparisonId) {
        return optionsDao.getOptionsByComparisonId(comparisonId)
                .doOnNext(options -> {
                    Log.i(LOG_DATA, "Retrieving " + Option.class.getSimpleName() + "[" + options.size() + "]");
                });
    }

    @Override
    public Flowable<Option> getOptionById(String optionId) {
        return optionsDao.getOptionById(optionId)
                .doOnNext(option -> {
                    Log.i(LOG_DATA, "Retrieving " + option);
                });
    }

    @Override
    public Completable insertOption(Option option) {
        return new CompletableFromAction(() -> {
            List<Option> options = optionsDao.getOptionsByComparisonId(option.getComparisonId()).blockingFirst();
            Log.i(LOG_DATA, "Inserting " + option);

            List<OptionComparison> optionComparisons = new ArrayList<>();
            for (Option o : options) {
                OptionComparison optionComparison = new OptionComparison(option.getId(), o.getId(), 0);
                Log.i(LOG_DATA, "Inserting " + optionComparison);
                optionComparisons.add(optionComparison);
            }

            optionsDao.insertOptionWithComparisons(option, optionComparisons);
        });
    }

    @Override
    public Completable updateOption(Option option) {
        return new CompletableFromAction(() -> {
            Log.i(LOG_DATA, "Updating " + option);
            optionsDao.updateOption(option);
        });
    }

    @Override
    public Completable deleteOption(Option option) {
        return new CompletableFromAction(() -> {
            Log.i(LOG_DATA, "Deleting " + option);
            optionsDao.deleteOption(option);
        });
    }

    @Override
    public Flowable<List<OptionComparisonEntry>> getOptionComparisonEntriesByComparisonId(String comparisonId) {
        return optionsDao.getOptionComparisonEntriesByComparisonId(comparisonId)
                .doOnNext(optionComparisonEntries -> {
                    Log.i(LOG_DATA, "Retrieving " + OptionComparisonEntry.class.getSimpleName() + "[" + optionComparisonEntries.size() + "]");
                });
    }

    @Override
    public Completable updateOptionComparisons(List<OptionComparisonEntry> optionComparisonEntries) {
        return new CompletableFromAction(() -> {
            List<OptionComparison> optionComparisons = new ArrayList<>(optionComparisonEntries.size());
            for (OptionComparisonEntry entry : optionComparisonEntries) {

                Log.i(LOG_DATA, "Creating " + OptionComparison.class.getSimpleName() + " from " + entry);
                OptionComparison optionComparison = new OptionComparison();
                optionComparison.setId(entry.getId());
                optionComparison.setFirstOptionId(entry.getFirstOption().getId());
                optionComparison.setSecondOptionId(entry.getSecondOption().getId());
                optionComparison.setProgress(entry.getProgress());

                optionComparisons.add(optionComparison);
            }
            Log.i(LOG_DATA, "Updating " + optionComparisons);
            optionsDao.updateOptionComparisons(optionComparisons.toArray(new OptionComparison[optionComparisons.size()]));
        });
    }
}
