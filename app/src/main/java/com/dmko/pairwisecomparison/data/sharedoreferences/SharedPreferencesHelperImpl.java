package com.dmko.pairwisecomparison.data.sharedoreferences;

import android.content.SharedPreferences;

public class SharedPreferencesHelperImpl implements SharedPreferencesHelper {

    private static final String PROMPT = "prompt";

    private SharedPreferences prefs;

    public SharedPreferencesHelperImpl(SharedPreferences prefs) {
        this.prefs = prefs;
    }

    @Override
    public void savePromptText(String prompt) {
        prefs.edit()
                .putString(PROMPT, prompt)
                .apply();
    }

    @Override
    public String getPromptText() {
        return prefs.getString(PROMPT, null);
    }
}
