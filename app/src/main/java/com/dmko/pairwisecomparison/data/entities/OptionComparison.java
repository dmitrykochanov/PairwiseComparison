package com.dmko.pairwisecomparison.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.UUID;

import static com.dmko.pairwisecomparison.utils.DatabaseSchema.OptionComparisons.Cols;
import static com.dmko.pairwisecomparison.utils.DatabaseSchema.OptionComparisons.TABLE_NAME;
import static com.dmko.pairwisecomparison.utils.DatabaseSchema.Options;

@Entity(tableName = TABLE_NAME, foreignKeys = {
        @ForeignKey(
                entity = Option.class,
                parentColumns = Options.Cols.ID,
                childColumns = Cols.FIRST_OPTION_ID,
                onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
                entity = Option.class,
                parentColumns = Options.Cols.ID,
                childColumns = Cols.SECOND_OPTION_ID,
                onDelete = ForeignKey.CASCADE
        )},
        indices = {
                @Index(Cols.FIRST_OPTION_ID),
                @Index(Cols.SECOND_OPTION_ID)
        })
public class OptionComparison {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = Cols.ID)
    private String id;

    @ColumnInfo(name = Cols.FIRST_OPTION_ID)
    private String firstOptionId;

    @ColumnInfo(name = Cols.SECOND_OPTION_ID)
    private String secondOptionId;

    @ColumnInfo(name = Cols.PROGRESS)
    private int progress;

    @Ignore
    public OptionComparison() {
        id = UUID.randomUUID().toString();
    }

    @Ignore
    public OptionComparison(String firstOptionId, String secondOptionId, int progress) {
        this();
        this.firstOptionId = firstOptionId;
        this.secondOptionId = secondOptionId;
        this.progress = progress;
    }

    public OptionComparison(@NonNull String id, String firstOptionId, String secondOptionId, int progress) {
        this.id = id;
        this.firstOptionId = firstOptionId;
        this.secondOptionId = secondOptionId;
        this.progress = progress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstOptionId() {
        return firstOptionId;
    }

    public void setFirstOptionId(String firstOptionId) {
        this.firstOptionId = firstOptionId;
    }

    public String getSecondOptionId() {
        return secondOptionId;
    }

    public void setSecondOptionId(String secondOptionId) {
        this.secondOptionId = secondOptionId;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    @Override
    public String toString() {
        return "OptionComparison{" +
                "id='" + id + '\'' +
                ", firstOptionId='" + firstOptionId + '\'' +
                ", secondOptionId='" + secondOptionId + '\'' +
                ", progress=" + progress +
                '}';
    }
}
