package com.dmko.pairwisecomparison.data.entities;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.dmko.pairwisecomparison.utils.DatabaseSchema.Options.Cols;

import java.util.UUID;

import static com.dmko.pairwisecomparison.utils.DatabaseSchema.Comparisons;
import static com.dmko.pairwisecomparison.utils.DatabaseSchema.Options.TABLE_NAME;

@Entity(tableName = TABLE_NAME, foreignKeys = {
        @ForeignKey(
                entity = Comparison.class,
                parentColumns = Comparisons.Cols.ID,
                childColumns = Cols.COMPARISON_ID,
                onDelete = ForeignKey.CASCADE)},
        indices = {
                @Index(Cols.COMPARISON_ID)
        })


public class Option implements Comparable<Option> {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = Cols.ID)
    private String id;

    @ColumnInfo(name = Cols.COMPARISON_ID)
    private String comparisonId;

    @ColumnInfo(name = Cols.NAME)
    private String name;


    @Ignore
    public Option() {
        id = UUID.randomUUID().toString();
    }

    @Ignore
    public Option(String comparisonId, String name) {
        this();
        this.comparisonId = comparisonId;
        this.name = name;
    }

    public Option(String id, String comparisonId, String name) {
        this.id = id;
        this.comparisonId = comparisonId;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComparisonId() {
        return comparisonId;
    }

    public void setComparisonId(String comparisonId) {
        this.comparisonId = comparisonId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Option{" +
                "id='" + id + '\'' +
                ", comparisonId='" + comparisonId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Option option = (Option) o;

        if (!id.equals(option.id)) return false;
        if (comparisonId != null ? !comparisonId.equals(option.comparisonId) : option.comparisonId != null)
            return false;
        return name != null ? name.equals(option.name) : option.name == null;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (comparisonId != null ? comparisonId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(@NonNull Option option) {
        return name.compareTo(option.getName());
    }
}
