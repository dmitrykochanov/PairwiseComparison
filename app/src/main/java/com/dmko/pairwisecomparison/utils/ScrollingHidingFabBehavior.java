package com.dmko.pairwisecomparison.utils;


import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;

import com.dmko.pairwisecomparison.R;

public class ScrollingHidingFabBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {

    public ScrollingHidingFabBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton fab, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton fab, View dependency) {
        if (dependency instanceof AppBarLayout) {

            int toolbarHeight = dependency.findViewById(R.id.toolbar).getHeight();
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
            int fabBottomMargin = lp.bottomMargin;
            int distanceToScroll = fab.getHeight() + fabBottomMargin;
            float ratio = (dependency.getY()) / (float) toolbarHeight;
            fab.setTranslationY(-distanceToScroll * ratio);
        }
        return true;
    }
}
