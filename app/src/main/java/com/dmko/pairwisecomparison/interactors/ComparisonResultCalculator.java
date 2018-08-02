package com.dmko.pairwisecomparison.interactors;

import com.dmko.pairwisecomparison.data.entities.Option;
import com.dmko.pairwisecomparison.data.entities.OptionComparisonEntry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

import static com.dmko.pairwisecomparison.utils.LogTags.LOG_APP;

public class ComparisonResultCalculator {

    public Map<Option, Integer> calculateComparisonResult(List<OptionComparisonEntry> optionComparisonEntries) {
        Timber.tag(LOG_APP);
        Timber.i("Calculating option results for %s[%d]", OptionComparisonEntry.class.getSimpleName(), optionComparisonEntries.size());

        Map<Option, Integer> results = new HashMap<>();
        for (OptionComparisonEntry entry : optionComparisonEntries) {
            if (!results.containsKey(entry.getFirstOption())) {
                results.put(entry.getFirstOption(), 0);
            }
            if (!results.containsKey(entry.getSecondOption())) {
                results.put(entry.getSecondOption(), 0);
            }

            Option key = entry.getProgress() < 0 ? entry.getFirstOption() : entry.getSecondOption();

            Integer currentProgress = results.get(key);
            currentProgress += Math.abs(entry.getProgress());
            results.put(key, currentProgress);
        }

        int progressSum = 0;
        for (Integer progress : results.values()) {
            progressSum += progress;
        }

        if (progressSum != 0) {
            for (Option option : results.keySet()) {
                int progressPercentage = Math.round((float) results.get(option) / progressSum * 100);
                results.put(option, progressPercentage);
            }
        } else {
            results.clear();
        }
        return results;
    }
}
