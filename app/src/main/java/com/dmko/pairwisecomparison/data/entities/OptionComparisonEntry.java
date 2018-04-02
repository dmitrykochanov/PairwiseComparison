package com.dmko.pairwisecomparison.data.entities;


import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Ignore;
import android.support.annotation.NonNull;

public class OptionComparisonEntry implements Comparable<OptionComparisonEntry> {

    private String id;

    @Embedded(prefix = "first_")
    private Option firstOption;

    @Embedded(prefix = "second_")
    private Option secondOption;


    private int progress;

    public OptionComparisonEntry() {
    }

    @Ignore
    public OptionComparisonEntry(String id, Option firstOption, Option secondOption, int progress) {
        this.id = id;
        this.firstOption = firstOption;
        this.secondOption = secondOption;
        this.progress = progress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Option getFirstOption() {
        return firstOption;
    }

    public void setFirstOption(Option firstOption) {
        this.firstOption = firstOption;
    }

    public Option getSecondOption() {
        return secondOption;
    }

    public void setSecondOption(Option secondOption) {
        this.secondOption = secondOption;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OptionComparisonEntry entry = (OptionComparisonEntry) o;

        return id.equals(entry.id) && firstOption.equals(entry.firstOption) && secondOption.equals(entry.secondOption);
    }

    @Override
    public String toString() {
        return "OptionComparisonEntry{" +
                "id='" + id + '\'' +
                ", firstOption=" + firstOption +
                ", secondOption=" + secondOption +
                ", progress=" + progress +
                '}';
    }

    @Override
    public int compareTo(@NonNull OptionComparisonEntry entry) {
        return firstOption.compareTo(entry.getFirstOption());
    }
}
