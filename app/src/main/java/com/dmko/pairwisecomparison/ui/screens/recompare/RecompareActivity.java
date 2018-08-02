package com.dmko.pairwisecomparison.ui.screens.recompare;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;

import com.dmko.pairwisecomparison.R;
import com.dmko.pairwisecomparison.ui.base.mvp.impl.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecompareActivity extends BaseActivity {

    private static final String EXTRA_COMPARISON_NAME = "com.dmko.comparison_name";
    private static final String EXTRA_COMPARISON_ID = "com.dmko.comparison_id";

    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recompare);
        ButterKnife.bind(this);

        String comparisonName = getIntent().getStringExtra(EXTRA_COMPARISON_NAME);
        String comparisonId = getIntent().getStringExtra(EXTRA_COMPARISON_ID);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(comparisonName);

        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentById(R.id.layout_fragment_container) == null) {
            final RecompareFragment fragment = RecompareFragment.newInstance(comparisonId);

            fm.beginTransaction()
                    .add(R.id.layout_fragment_container, fragment)
                    .commit();
        }
    }

    public static Intent newIntent(Context context, String comparisonId, String comparisonName) {
        Intent intent = new Intent(context, RecompareActivity.class);
        intent.putExtra(EXTRA_COMPARISON_NAME, comparisonName);
        intent.putExtra(EXTRA_COMPARISON_ID, comparisonId);
        return intent;
    }
}
