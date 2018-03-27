package com.dmko.pairwisecomparison.data.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dmko.pairwisecomparison.data.entities.Comparison;

import java.util.List;

import io.reactivex.Flowable;

import static com.dmko.pairwisecomparison.utils.DatabaseSchema.Comparisons;

@Dao
public interface ComparisonsDao {

    @Query("SELECT * FROM " + Comparisons.TABLE_NAME)
    Flowable<List<Comparison>> getComparisons();

    @Query("SELECT * FROM " + Comparisons.TABLE_NAME + " WHERE " + Comparisons.Cols.ID + " = :comparisonId LIMIT 1")
    Flowable<Comparison> getComparisonById(String comparisonId);

    @Insert
    void insertComparison(Comparison comparison);

    @Update
    void updateComparison(Comparison comparison);

    @Delete
    void deleteComparison(Comparison comparison);
}
