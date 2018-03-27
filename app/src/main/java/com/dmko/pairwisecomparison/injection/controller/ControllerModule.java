package com.dmko.pairwisecomparison.injection.controller;


import com.dmko.pairwisecomparison.injection.scopes.ControllerScope;
import com.dmko.pairwisecomparison.ui.screens.comparison.optioncomparisons.OptionComparisonsContract;
import com.dmko.pairwisecomparison.ui.screens.comparison.optioncomparisons.recyclerview.OptionComparisonsAdapter;
import com.dmko.pairwisecomparison.ui.screens.comparison.options.OptionsContract;
import com.dmko.pairwisecomparison.ui.screens.comparison.options.recyclerview.OptionsAdapter;
import com.dmko.pairwisecomparison.ui.screens.comparisons.ComparisonsContract;
import com.dmko.pairwisecomparison.ui.screens.comparisons.recyclerview.ComparisonsAdapter;

import dagger.Module;
import dagger.Provides;

@Module
public class ControllerModule {

    @Provides
    @ControllerScope
    public ComparisonsAdapter provideComparisonsAdapter(ComparisonsContract.Presenter presenter) {
        return new ComparisonsAdapter(presenter);
    }

    @Provides
    @ControllerScope
    public OptionsAdapter provideOptionsAdapter(OptionsContract.Presenter presenter) {
        return new OptionsAdapter(presenter);
    }

    @Provides
    @ControllerScope
    public OptionComparisonsAdapter provideOptionComparisonsAdapter(OptionComparisonsContract.Presenter presenter) {
        return new OptionComparisonsAdapter(presenter);
    }
}