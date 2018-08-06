package com.dmko.pairwisecomparison.ui.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;

import com.dmko.pairwisecomparison.R;
import com.dmko.pairwisecomparison.ui.base.mvp.impl.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class SingleFragmentActivity extends BaseActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.layout_fragment_container);
        if (fragment == null) {
            fragment = getFragment();
            fm.beginTransaction().add(R.id.layout_fragment_container, fragment).commit();
        }
    }

    protected abstract Fragment getFragment();
}
