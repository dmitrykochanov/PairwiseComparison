package com.dmko.pairwisecomparison.ui.screens.comparison;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.dmko.pairwisecomparison.R;
import com.dmko.pairwisecomparison.ui.screens.addeditoption.AddEditOptionDialog;
import com.dmko.pairwisecomparison.ui.screens.comparison.comparisonresult.ComparisonResultFragment;
import com.dmko.pairwisecomparison.ui.screens.comparison.optioncomparisons.OptionComparisonsFragment;
import com.dmko.pairwisecomparison.ui.screens.comparison.options.OptionsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ComparisonActivity extends AppCompatActivity {

    private static final String EXTRA_COMP_ID = "com.dmko.comp_id";
    private static final String EXTRA_COMP_NAME = "com.dmko.comp_name";
    private static final String TAG_DIALOG = "dialog";

    private static final int NUMBER_OF_TABS = 3;
    private static final int COMPARISON_RESULT_TAB = 0;
    private static final int OPTIONS_TAB = 1;
    private static final int OPTION_COMPARISON_TAB = 2;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @BindView(R.id.appbar) AppBarLayout appBar;
    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.fab_add) FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comparison);
        ButterKnife.bind(this);

        String comparisonId = getIntent().getStringExtra(EXTRA_COMP_ID);
        String comparisonName = getIntent().getStringExtra(EXTRA_COMP_NAME);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(comparisonName);

        fabAdd.hide();
        fabAdd.setOnClickListener(v -> {
            showOptionDialog(comparisonId);
        });

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case COMPARISON_RESULT_TAB:
                        return ComparisonResultFragment.newInstance(comparisonId, comparisonName);
                    case OPTIONS_TAB:
                        return OptionsFragment.newInstance(comparisonId);
                    case OPTION_COMPARISON_TAB:
                        return OptionComparisonsFragment.newInstance(comparisonId);
                    default:
                        return null;
                }
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case COMPARISON_RESULT_TAB:
                        return getString(R.string.tab_result);
                    case OPTIONS_TAB:
                        return getString(R.string.tap_options);
                    case OPTION_COMPARISON_TAB:
                        return getString(R.string.tab_option_comparison);
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return NUMBER_OF_TABS;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case COMPARISON_RESULT_TAB:
                        appBar.setExpanded(true);
                        fabAdd.hide();
                        break;
                    case OPTIONS_TAB:
                    case OPTION_COMPARISON_TAB:
                        fabAdd.show();
                        break;
                }
            }
        });

        tabLayout.setupWithViewPager(viewPager);
    }

    private void showOptionDialog(String comparisonId) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(TAG_DIALOG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        DialogFragment dialog = AddEditOptionDialog.newInstance(comparisonId, null);
        dialog.show(ft, TAG_DIALOG);
    }

    public static Intent getIntent(Context context, String comparisonId, String comparisonName) {
        Intent intent = new Intent(context, ComparisonActivity.class);
        intent.putExtra(EXTRA_COMP_ID, comparisonId);
        intent.putExtra(EXTRA_COMP_NAME, comparisonName);
        return intent;
    }
}
