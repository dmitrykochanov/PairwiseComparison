package com.dmko.pairwisecomparison.ui.screens.recompare;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.dmko.pairwisecomparison.ui.base.SingleFragmentActivity;

public class RecompareActivity extends SingleFragmentActivity {

    private static final String EXTRA_COMPARISON_NAME = "com.dmko.comparison_name";
    private static final String EXTRA_COMPARISON_ID = "com.dmko.comparison_id";

    @Override
    protected Fragment getFragment() {
        String comparisonName = getIntent().getStringExtra(EXTRA_COMPARISON_NAME);
        String comparisonId = getIntent().getStringExtra(EXTRA_COMPARISON_ID);

        setTitle(comparisonName);

        return RecompareFragment.newInstance(comparisonId);
    }

    public static Intent newIntent(Context context, String comparisonId, String comparisonName) {
        Intent intent = new Intent(context, RecompareActivity.class);
        intent.putExtra(EXTRA_COMPARISON_NAME, comparisonName);
        intent.putExtra(EXTRA_COMPARISON_ID, comparisonId);
        return intent;
    }
}
