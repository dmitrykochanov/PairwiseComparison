package com.dmko.pairwisecomparison.data;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.dmko.pairwisecomparison.data.dao.ComparisonsDao;
import com.dmko.pairwisecomparison.data.dao.OptionsDao;
import com.dmko.pairwisecomparison.data.entities.Comparison;
import com.dmko.pairwisecomparison.data.entities.Option;
import com.dmko.pairwisecomparison.data.entities.OptionComparison;
import com.dmko.pairwisecomparison.utils.DatabaseSchema;

@Database(entities = {Comparison.class, Option.class, OptionComparison.class}, version = DatabaseSchema.VERSION, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ComparisonsDao getComparisonDao();

    public abstract OptionsDao getOptionsDao();
}
