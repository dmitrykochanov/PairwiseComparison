package com.dmko.pairwisecomparison.injection.application;


import android.arch.persistence.room.Room;
import android.content.Context;

import com.dmko.pairwisecomparison.data.AppDatabase;
import com.dmko.pairwisecomparison.data.dao.ComparisonsDao;
import com.dmko.pairwisecomparison.data.dao.OptionsDao;
import com.dmko.pairwisecomparison.injection.scopes.ApplicationScope;
import com.dmko.pairwisecomparison.utils.DatabaseSchema;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {

    @Provides
    @ApplicationScope
    public AppDatabase provideDatabase(Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, DatabaseSchema.DATABASE_NAME).build();
    }

    @Provides
    @ApplicationScope
    public ComparisonsDao provideComparisonsDao(AppDatabase appDatabase) {
        return appDatabase.getComparisonDao();
    }

    @Provides
    @ApplicationScope
    public OptionsDao provideOptionsDao(AppDatabase appDatabase) {
        return appDatabase.getOptionsDao();
    }
}
