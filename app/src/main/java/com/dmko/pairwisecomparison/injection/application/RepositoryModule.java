package com.dmko.pairwisecomparison.injection.application;

import com.dmko.pairwisecomparison.data.dao.ComparisonsDao;
import com.dmko.pairwisecomparison.data.dao.OptionsDao;
import com.dmko.pairwisecomparison.data.repositories.ComparisonRepository;
import com.dmko.pairwisecomparison.data.repositories.OptionsRepository;
import com.dmko.pairwisecomparison.data.repositories.impl.ComparisonRepositoryImpl;
import com.dmko.pairwisecomparison.data.repositories.impl.OptionsRepositoryImpl;
import com.dmko.pairwisecomparison.data.sharedoreferences.SharedPreferencesHelper;
import com.dmko.pairwisecomparison.injection.scopes.ApplicationScope;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Provides
    @ApplicationScope
    public ComparisonRepository provideComparisonRepository(ComparisonsDao comparisonsDao) {
        return new ComparisonRepositoryImpl(comparisonsDao);
    }

    @Provides
    @ApplicationScope
    public OptionsRepository provideOptionsRepository(OptionsDao optionsDao, SharedPreferencesHelper sharedPreferencesHelper) {
        return new OptionsRepositoryImpl(optionsDao, sharedPreferencesHelper);
    }
}
