package com.dmko.pairwisecomparison.data.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import com.dmko.pairwisecomparison.data.entities.Option;
import com.dmko.pairwisecomparison.data.entities.OptionComparison;
import com.dmko.pairwisecomparison.data.entities.OptionComparisonEntry;

import java.util.List;

import io.reactivex.Flowable;

import static com.dmko.pairwisecomparison.utils.DatabaseSchema.OptionComparisons;
import static com.dmko.pairwisecomparison.utils.DatabaseSchema.Options;

@Dao
public abstract class OptionsDao {

    @Query("SELECT * FROM " + Options.TABLE_NAME + " WHERE " + Options.Cols.COMPARISON_ID + " = :comparisonId")
    public abstract Flowable<List<Option>> getOptionsByComparisonId(String comparisonId);

    @Query("SELECT * FROM " + Options.TABLE_NAME + " WHERE " + Options.Cols.ID + " = :optionId LIMIT 1")
    public abstract Flowable<Option> getOptionById(String optionId);

    @Insert
    public abstract void insertOption(Option option);

    @Insert
    public abstract void insertOptions(List<Option> options);

    @Update
    public abstract void updateOption(Option option);

    @Delete
    public abstract void deleteOption(Option option);

    @Query("SELECT comp." + OptionComparisons.Cols.ID +
            ", comp." + OptionComparisons.Cols.PROGRESS +
            ", first." + Options.Cols.ID + " AS first_id" +
            ", first." + Options.Cols.COMPARISON_ID + " AS first_comparison_id" +
            ", first." + Options.Cols.NAME + " AS first_name" +
            ", second." + Options.Cols.ID + " AS second_id" +
            ", second." + Options.Cols.COMPARISON_ID + " AS second_comparison_id" +
            ", second." + Options.Cols.NAME + " AS second_name" +
            " FROM " + OptionComparisons.TABLE_NAME + " comp " +
            " JOIN " + Options.TABLE_NAME + " first " +
            " ON comp." + OptionComparisons.Cols.FIRST_OPTION_ID + " = first." + Options.Cols.ID +
            " JOIN " + Options.TABLE_NAME + " second" +
            " ON comp." + OptionComparisons.Cols.SECOND_OPTION_ID + " = second." + Options.Cols.ID +
            " WHERE first." + Options.Cols.COMPARISON_ID + " = :comparisonId")
    public abstract Flowable<List<OptionComparisonEntry>> getOptionComparisonEntriesByComparisonId(String comparisonId);

    @Insert
    public abstract void insertOptionComparisons(OptionComparison... optionComparisons);

    @Update
    public abstract void updateOptionComparison(OptionComparison optionComparison);

    @Transaction
    public void insertOptionWithComparisons(Option option, List<OptionComparison> optionComparisons) {
        insertOption(option);
        insertOptionComparisons(optionComparisons.toArray(new OptionComparison[optionComparisons.size()]));
    }
}
