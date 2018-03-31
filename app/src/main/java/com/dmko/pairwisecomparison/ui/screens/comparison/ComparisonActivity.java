package com.dmko.pairwisecomparison.ui.screens.comparison;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.dmko.pairwisecomparison.R;
import com.dmko.pairwisecomparison.ui.screens.comparison.comparisonresult.ComparisonResultFragment;
import com.dmko.pairwisecomparison.ui.screens.comparison.optioncomparisons.OptionComparisonsFragment;
import com.dmko.pairwisecomparison.ui.screens.comparison.options.OptionsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ComparisonActivity extends AppCompatActivity {
    private static final String EXTRA_COMP_ID = "com.dmko.comp_id";
    private static final String EXTRA_COMP_NAME = "com.dmko.comp_name";

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @BindView(R.id.appbar) AppBarLayout appBar;
    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.fab_add) FloatingActionButton fabAdd;

    ComparisonResultFragment comparisonResultFragment;
    OptionsFragment optionsFragment;
    OptionComparisonsFragment optionComparisonsFragment;
    private int lastPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comparison);
        ButterKnife.bind(this);

        String comparisonId = getIntent().getStringExtra(EXTRA_COMP_ID);
        String comparisonName = getIntent().getStringExtra(EXTRA_COMP_NAME);
        comparisonResultFragment = ComparisonResultFragment.newInstance(comparisonId);
        optionsFragment = OptionsFragment.newInstance(comparisonId);
        optionComparisonsFragment = OptionComparisonsFragment.newInstance(comparisonId);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(comparisonName);


        fabAdd.hide();
        fabAdd.setOnClickListener(v -> {
            switch (viewPager.getCurrentItem()) {
                case 0:
                    break;
                case 1:
                    optionsFragment.onFabAddClicked();
                    break;
                case 2:
                    optionComparisonsFragment.onFabAddClicked();
                    break;
            }
        });

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return comparisonResultFragment;
                    case 1:
                        return optionsFragment;
                    case 2:
                        return optionComparisonsFragment;
                    default:
                        return null;
                }
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return getString(R.string.tab_result);
                    case 1:
                        return getString(R.string.tap_options);
                    case 2:
                        return getString(R.string.tab_compare);
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 1:
                        if (lastPosition == 2) {
                            optionComparisonsFragment.saveChanges();
                        }
                    case 2:
                        fabAdd.show();
                        break;
                    case 0:
                        if (lastPosition == 2) {
                            optionComparisonsFragment.saveChanges();
                        }
                        appBar.setExpanded(true);
                    default:
                        fabAdd.hide();
                }
                lastPosition = position;
            }
        });

        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (lastPosition == 2 && optionComparisonsFragment != null) {
            optionComparisonsFragment.saveChanges();
        }
    }

    public static Intent getIntent(Context context, String comparisonId, String comparisonName) {
        Intent intent = new Intent(context, ComparisonActivity.class);
        intent.putExtra(EXTRA_COMP_ID, comparisonId);
        intent.putExtra(EXTRA_COMP_NAME, comparisonName);
        return intent;
    }
}
