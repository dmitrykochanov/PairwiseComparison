package com.dmko.pairwisecomparison.injection.controller;


import com.dmko.pairwisecomparison.data.repositories.ComparisonRepository;
import com.dmko.pairwisecomparison.data.repositories.OptionsRepository;
import com.dmko.pairwisecomparison.injection.scopes.ControllerScope;
import com.dmko.pairwisecomparison.ui.screens.addeditcomparison.AddEditComparisonContract;
import com.dmko.pairwisecomparison.ui.screens.addeditcomparison.AddEditComparisonPresenter;
import com.dmko.pairwisecomparison.ui.screens.addeditoption.AddEditOptionContract;
import com.dmko.pairwisecomparison.ui.screens.addeditoption.AddEditOptionPresenter;
import com.dmko.pairwisecomparison.ui.screens.comparison.ComparisonContract;
import com.dmko.pairwisecomparison.ui.screens.comparison.comparisonresult.ComparisonResultContract;
import com.dmko.pairwisecomparison.ui.screens.comparison.comparisonresult.ComparisonResultPresenter;
import com.dmko.pairwisecomparison.ui.screens.comparison.optioncomparisons.OptionComparisonsContract;
import com.dmko.pairwisecomparison.ui.screens.comparison.optioncomparisons.OptionComparisonsPresenter;
import com.dmko.pairwisecomparison.ui.screens.comparison.options.ComparisonPresenter;
import com.dmko.pairwisecomparison.ui.screens.comparison.options.OptionsContract;
import com.dmko.pairwisecomparison.ui.screens.comparison.options.OptionsPresenter;
import com.dmko.pairwisecomparison.ui.screens.comparisons.ComparisonsContract;
import com.dmko.pairwisecomparison.ui.screens.comparisons.ComparisonsPresenter;
import com.dmko.pairwisecomparison.ui.screens.pasteoptions.PasteOptionsContract;
import com.dmko.pairwisecomparison.ui.screens.pasteoptions.PasteOptionsPresenter;
import com.dmko.pairwisecomparison.ui.screens.recompare.RecompareContract;
import com.dmko.pairwisecomparison.ui.screens.recompare.RecomparePresenter;
import com.dmko.pairwisecomparison.utils.SchedulersFacade;

import dagger.Module;
import dagger.Provides;

@Module
public class PresenterModule {

    @Provides
    @ControllerScope
    public ComparisonsContract.Presenter provideComparisonsPresenter(SchedulersFacade schedulers, ComparisonRepository comparisonRepository) {
        return new ComparisonsPresenter(schedulers, comparisonRepository);
    }

    @Provides
    @ControllerScope
    public AddEditComparisonContract.Presenter provideAddEditComparisonContractPresenter(SchedulersFacade schedulers, ComparisonRepository comparisonRepository) {
        return new AddEditComparisonPresenter(schedulers, comparisonRepository);
    }

    @Provides
    @ControllerScope
    public ComparisonResultContract.Presenter provideComparisonResultPresenter(SchedulersFacade schedulers, OptionsRepository optionsRepository) {
        return new ComparisonResultPresenter(schedulers, optionsRepository);
    }

    @Provides
    @ControllerScope
    public OptionsContract.Presenter provideOptionsPresenter(SchedulersFacade schedulers, OptionsRepository optionsRepository) {
        return new OptionsPresenter(schedulers, optionsRepository);
    }

    @Provides
    @ControllerScope
    public OptionComparisonsContract.Presenter provideOptionComparisonsPresenter(SchedulersFacade schedulers, OptionsRepository optionsRepository) {
        return new OptionComparisonsPresenter(schedulers, optionsRepository);
    }

    @Provides
    @ControllerScope
    public AddEditOptionContract.Presenter provideAddEditOptionPresenter(SchedulersFacade schedulers, OptionsRepository optionsRepository) {
        return new AddEditOptionPresenter(schedulers, optionsRepository);
    }

    @Provides
    @ControllerScope
    public PasteOptionsContract.Presenter providePasteOptionsPresenter(SchedulersFacade schedulers, OptionsRepository optionsRepository) {
        return new PasteOptionsPresenter(schedulers, optionsRepository);
    }

    @Provides
    @ControllerScope
    public RecompareContract.Presenter provideRecomparePresenter(SchedulersFacade schedulers, OptionsRepository optionsRepository) {
        return new RecomparePresenter(schedulers, optionsRepository);
    }

    @Provides
    @ControllerScope
    public ComparisonContract.Presenter provideComparePresenter(SchedulersFacade schedulers, OptionsRepository optionsRepository) {
        return new ComparisonPresenter(schedulers, optionsRepository);
    }
}
