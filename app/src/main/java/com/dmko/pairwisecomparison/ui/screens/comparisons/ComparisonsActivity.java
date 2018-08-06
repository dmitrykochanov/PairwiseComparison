package com.dmko.pairwisecomparison.ui.screens.comparisons;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.dmko.pairwisecomparison.R;
import com.dmko.pairwisecomparison.ui.base.mvp.impl.BaseActivity;
import com.dmko.pairwisecomparison.ui.screens.licenses.LicensesDialog;
import com.dmko.pairwisecomparison.ui.screens.settings.SettingsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ComparisonsActivity extends BaseActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fab_add) FloatingActionButton fabAdd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comparisons);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentById(R.id.layout_fragment_container) == null) {
            final ComparisonsFragment fragment = new ComparisonsFragment();

            fabAdd.setOnClickListener(v -> {
                fragment.onFabAddClicked();
            });

            fm.beginTransaction()
                    .add(R.id.layout_fragment_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_licenses:
                showDialog(new LicensesDialog());
                return true;
            case R.id.item_settings:
                startActivity(SettingsActivity.getIntent(this));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
