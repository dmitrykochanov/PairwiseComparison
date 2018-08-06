package com.dmko.pairwisecomparison.ui.screens.settings;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.dmko.pairwisecomparison.R;
import com.dmko.pairwisecomparison.ui.base.SingleFragmentActivity;

public class SettingsActivity extends SingleFragmentActivity {

    @Override
    protected Fragment getFragment() {
        setTitle(getString(R.string.title_settings));
        return new SettingsFragment();
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, SettingsActivity.class);
    }
}
