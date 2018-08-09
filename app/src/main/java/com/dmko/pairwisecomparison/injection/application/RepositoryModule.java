package com.dmko.pairwisecomparison.injection.application;

import com.dmko.pairwisecomparison.data.dao.ComparisonsDao;
import com.dmko.pairwisecomparison.data.dao.OptionsDao;
import com.dmko.pairwisecomparison.data.repositories.ComparisonRepository;
import com.dmko.pairwisecomparison.data.repositories.OptionsRepository;
import com.dmko.pairwisecomparison.data.repositories.SettingsRepository;
import com.dmko.pairwisecomparison.data.repositories.impl.ComparisonRepositoryImpl;
import com.dmko.pairwisecomparison.data.repositories.impl.OptionsRepositoryImpl;
import com.dmko.pairwisecomparison.data.repositories.impl.SettingsRepositoryImpl;
import com.dmko.pairwisecomparison.data.sharedoreferences.SharedPreferencesHelper;
import com.dmko.pairwisecomparison.injection.scopes.ApplicationScope;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Provides
    @ApplicationScope
    public ComparisonRepository provideComparisonRepository(ComparisonsDao comparisonsDao, OptionsDao optionsDao) {
        return new ComparisonRepositoryImpl(comparisonsDao, optionsDao);
    }

    @Provides
    @ApplicationScope
    public OptionsRepository provideOptionsRepository(OptionsDao optionsDao) {
        return new OptionsRepositoryImpl(optionsDao);
    }

    @Provides
    @ApplicationScope
    public SettingsRepository provideSettingsRepository(SharedPreferencesHelper prefs) {
        return new SettingsRepositoryImpl(prefs);
    }
}
