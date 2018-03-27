package com.dmko.pairwisecomparison.ui.screens.comparison.optioncomparisons;

import android.util.Log;

import com.dmko.pairwisecomparison.data.entities.Option;
import com.dmko.pairwisecomparison.data.entities.OptionComparisonEntry;
import com.dmko.pairwisecomparison.data.repositories.OptionsRepository;
import com.dmko.pairwisecomparison.ui.base.mvp.impl.BasePresenterImpl;
import com.dmko.pairwisecomparison.utils.SchedulersFacade;

import java.util.Collections;
import java.util.List;

import static com.dmko.pairwisecomparison.utils.LogTags.LOG_APP;

public class OptionComparisonsPresenter extends BasePresenterImpl<OptionComparisonsContract.View> implements OptionComparisonsContract.Presenter {
    private SchedulersFacade schedulers;
    private OptionsRepository optionsRepository;
    private String comparisonId;

    public OptionComparisonsPresenter(SchedulersFacade schedulers, OptionsRepository optionsRepository) {
        this.schedulers = schedulers;
        this.optionsRepository = optionsRepository;
    }

    @Override
    public void start(String comparisonId) {
        Log.i(LOG_APP, "Starting " + OptionComparisonsPresenter.class.getSimpleName());
        Log.i(LOG_APP, "Comparison id = " + comparisonId);

        this.comparisonId = comparisonId;
        getView().showLoading(true);
        addDisposable(optionsRepository.getOptionComparisonEntriesByComparisonId(comparisonId)
                .doOnNext(Collections::sort)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui())
                .subscribe(optionComparisons -> {
                    Log.i(LOG_APP, "Sending " + Option.class.getSimpleName() + "[" + optionComparisons.size() + "] to the view");
                    if (isViewAttached()) {
                        getView().setOptionComparisons(optionComparisons);
                        getView().showLoading(false);
                    }
                }));
    }

    @Override
    public void updateOptionComparisons(List<OptionComparisonEntry> optionComparisons) {
        addDisposable(optionsRepository.updateOptionComparisons(optionComparisons)
                .subscribeOn(schedulers.io())
                .subscribe());
    }

    @Override
    public void onAddOptionSelected() {
        getView().showOptionDialog(comparisonId);
    }
}
