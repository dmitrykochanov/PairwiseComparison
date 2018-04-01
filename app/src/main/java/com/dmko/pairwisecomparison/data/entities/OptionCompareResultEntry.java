package com.dmko.pairwisecomparison.data.entities;

import java.util.Map;

public class OptionCompareResultEntry {
    private Map<Option, Integer> results;
    private int chartType;

    public OptionCompareResultEntry() {
    }

    public OptionCompareResultEntry(Map<Option, Integer> results, int chartType) {
        this.results = results;
        this.chartType = chartType;
    }

    public Map<Option, Integer> getResults() {
        return results;
    }

    public void setResults(Map<Option, Integer> results) {
        this.results = results;
    }

    public int getChartType() {
        return chartType;
    }

    public void setChartType(int chartType) {
        this.chartType = chartType;
    }
}
