package com.dmko.pairwisecomparison.injection.controller;

import com.dmko.pairwisecomparison.injection.scopes.ControllerScope;
import com.dmko.pairwisecomparison.interactors.ComparisonResultCalculator;

import dagger.Module;
import dagger.Provides;

@Module
public class InteractorModule {

    @Provides
    @ControllerScope
    public ComparisonResultCalculator provideComparisonResultCalculator() {
        return new ComparisonResultCalculator();
    }
}
