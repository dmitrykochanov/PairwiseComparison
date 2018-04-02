package com.dmko.pairwisecomparison.ui.screens.comparison.optioncomparisons;

import com.dmko.pairwisecomparison.data.entities.Option;
import com.dmko.pairwisecomparison.data.entities.OptionComparisonEntry;
import com.dmko.pairwisecomparison.data.repositories.OptionsRepository;
import com.dmko.pairwisecomparison.ui.base.mvp.impl.BasePresenterImpl;
import com.dmko.pairwisecomparison.ui.screens.comparison.optioncomparisons.filters.AllFilter;
import com.dmko.pairwisecomparison.ui.screens.comparison.optioncomparisons.filters.HasOptionFilter;
import com.dmko.pairwisecomparison.ui.screens.comparison.optioncomparisons.filters.NotComparedFilter;
import com.dmko.pairwisecomparison.ui.screens.comparison.optioncomparisons.filters.OptionComparisonEntryFilter;
import com.dmko.pairwisecomparison.utils.SchedulersFacade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

import static com.dmko.pairwisecomparison.utils.LogTags.LOG_APP;

public class OptionComparisonsPresenter extends BasePresenterImpl<OptionComparisonsContract.View> implements OptionComparisonsContract.Presenter {
    private SchedulersFacade schedulers;
    private OptionsRepository optionsRepository;
    private String comparisonId;
    private PublishSubject<OptionComparisonEntryFilter> filterTypeSubject;

    public OptionComparisonsPresenter(SchedulersFacade schedulers, OptionsRepository optionsRepository) {
        this.schedulers = schedulers;
        this.optionsRepository = optionsRepository;
        this.filterTypeSubject = PublishSubject.create();
    }

    @Override
    public void start(String comparisonId) {
        Timber.tag(LOG_APP);
        Timber.i("Starting %s with comparisonId = %s", this.getClass().getSimpleName(), comparisonId);

        this.comparisonId = comparisonId;
        getView().showLoading(true);

        Flowable<List<OptionComparisonEntry>> entriesFlowable = optionsRepository.getOptionComparisonEntriesByComparisonId(comparisonId)
                .doOnNext(Collections::sort)
                .distinctUntilChanged();
        Flowable<OptionComparisonEntryFilter> filterTypeFlowable = filterTypeSubject.toFlowable(BackpressureStrategy.BUFFER);

        addDisposable(entriesFlowable
                .map(entries -> {
                    Set<Option> options = new HashSet<>();
                    for (OptionComparisonEntry entry : entries) {
                        options.add(entry.getFirstOption());
                        options.add(entry.getSecondOption());
                    }
                    List<Option> sortedOptions = new ArrayList<>(options);
                    Collections.sort(sortedOptions);

                    List<OptionComparisonEntryFilter> filters = new ArrayList<>();
                    for (Option option : sortedOptions) {
                        filters.add(new HasOptionFilter(option));
                    }
                    filters.add(0, new AllFilter());
                    filters.add(1, new NotComparedFilter());

                    return filters;
                })
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui())
                .subscribe(filers -> {
                    if (isViewAttached()) {
                        Timber.tag(LOG_APP);
                        Timber.i("Sending %s[%d] to the view", OptionComparisonEntryFilter.class.getSimpleName(), filers.size());
                        getView().setFilterTypes(filers);
                    }
                }));

        addDisposable(Flowable.combineLatest(entriesFlowable, filterTypeFlowable,
                (entries, filterType) -> {
                    return Observable.fromIterable(entries)
                            .filter(filterType::filter)
                            .toList()
                            .blockingGet();
                })
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui())
                .subscribe(entries -> {
                    if (isViewAttached()) {
                        Timber.tag(LOG_APP);
                        Timber.i("Sending %s[%d] to the view", OptionComparisonEntry.class, entries.size());
                        getView().setOptionComparisons(entries);
                        getView().showLoading(false);
                    }
                }));
    }

    @Override
    public void setFilter(OptionComparisonEntryFilter filter) {
        Timber.tag(LOG_APP);
        Timber.i("Filter selected: %s", filter);
        filterTypeSubject.onNext(filter);
    }

    @Override
    public void updateOptionComparison(OptionComparisonEntry optionComparison) {
        addDisposable(optionsRepository.updateOptionComparison(optionComparison)
                .subscribeOn(schedulers.io())
                .subscribe());
    }

    @Override
    public void onAddOptionSelected() {
        getView().showOptionDialog(comparisonId);
    }
}
