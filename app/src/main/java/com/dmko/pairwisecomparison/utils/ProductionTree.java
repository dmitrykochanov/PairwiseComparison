package com.dmko.pairwisecomparison.utils;

import timber.log.Timber;

public class ProductionTree extends Timber.Tree {
    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
    }
}
