package com.dmko.pairwisecomparison.data.entities;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.UUID;

import static com.dmko.pairwisecomparison.utils.DatabaseSchema.Comparisons.Cols;
import static com.dmko.pairwisecomparison.utils.DatabaseSchema.Comparisons.TABLE_NAME;

@Entity(tableName = TABLE_NAME)
public class Comparison implements Comparable<Comparison> {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = Cols.ID)
    private String id;

    @ColumnInfo(name = Cols.NAME)
    private String name;

    @Ignore
    public Comparison() {
        id = UUID.randomUUID().toString();
    }

    @Ignore
    public Comparison(String name) {
        this();
        this.name = name;
    }

    public Comparison(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Comparison{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public int compareTo(@NonNull Comparison comparison) {
        return name.compareTo(comparison.getName());
    }
}
