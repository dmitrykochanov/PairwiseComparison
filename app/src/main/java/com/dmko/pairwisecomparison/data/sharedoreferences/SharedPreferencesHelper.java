package com.dmko.pairwisecomparison.data.sharedoreferences;

public interface SharedPreferencesHelper {

    void savePromptText(String prompt);

    String getPromptText();
}
