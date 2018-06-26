package com.dmko.pairwisecomparison.ui.screens.comparison;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.dmko.pairwisecomparison.R;
import com.dmko.pairwisecomparison.ui.base.mvp.impl.BaseActivity;
import com.dmko.pairwisecomparison.ui.screens.comparison.comparisonresult.ComparisonResultFragment;
import com.dmko.pairwisecomparison.ui.screens.comparison.optioncomparisons.OptionComparisonsFragment;
import com.dmko.pairwisecomparison.ui.screens.comparison.options.OptionsFragment;
import com.dmko.pairwisecomparison.ui.screens.pasteoptions.PasteOptionsDialog;
import com.dmko.pairwisecomparison.ui.screens.recompare.RecompareActivity;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ComparisonActivity extends BaseActivity implements ComparisonContract.View {

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
    @BindView(R.id.floating_action_menu) FloatingActionMenu floatingActionMenu;
    @BindView(R.id.fab_compare_options) FloatingActionButton fabCompareOptions;
    @BindView(R.id.fab_add_options) FloatingActionButton fabAddOptions;

    @Inject ComparisonContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comparison);
        ButterKnife.bind(this);

        getControllerComponent().inject(this);
        presenter.attachView(this);

        String comparisonId = getIntent().getStringExtra(EXTRA_COMP_ID);
        String comparisonName = getIntent().getStringExtra(EXTRA_COMP_NAME);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(comparisonName);

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
                        break;
                }
            }
        });
        tabLayout.setupWithViewPager(viewPager);

        fabCompareOptions.setOnClickListener(v -> {
            presenter.onOpenRecompareActivitySelected(comparisonId, comparisonName);
        });

        fabAddOptions.setOnClickListener(v -> {
            openPasteOptionsDialog(comparisonId);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.stop();
        presenter.detachView();
    }

    private void openPasteOptionsDialog(String comparisonId) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = clipboardManager.getPrimaryClip();
        if (clip == null || clip.getItemAt(0).getText().toString().trim().isEmpty()) {
            Snackbar.make(toolbar, R.string.snackbar_empty_clipboard, Snackbar.LENGTH_LONG).show();
        } else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment prev = getSupportFragmentManager().findFragmentByTag(TAG_DIALOG);
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            DialogFragment dialog = PasteOptionsDialog.newInstance(comparisonId);
            dialog.show(ft, TAG_DIALOG);
        }
    }


    @Override
    public void showNothingToCompareDialog() {
        Snackbar.make(toolbar, R.string.snackbar_nothing_to_compare, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void openRecompareActivity(String comparisonId, String comparisonName) {
        Intent intent = RecompareActivity.newIntent(this, comparisonId, comparisonName);
        startActivity(intent);
    }

    public static Intent getIntent(Context context, String comparisonId, String comparisonName) {
        Intent intent = new Intent(context, ComparisonActivity.class);
        intent.putExtra(EXTRA_COMP_ID, comparisonId);
        intent.putExtra(EXTRA_COMP_NAME, comparisonName);
        return intent;
    }
}
