package com.dmko.pairwisecomparison.ui.base.mvp.impl;


import android.util.Log;

import com.dmko.pairwisecomparison.ui.base.mvp.BasePresenter;
import com.dmko.pairwisecomparison.ui.base.mvp.BaseView;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static com.dmko.pairwisecomparison.utils.LogTags.LOG_UI;

public class BasePresenterImpl<T extends BaseView> implements BasePresenter<T> {
    private T view;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public void attachView(T view) {
        this.view = view;
        Log.i(LOG_UI, "View " + view.getClass().getSimpleName() + " attached to " + this.getClass().getSimpleName());
    }

    @Override
    public void detachView() {
        Log.i(LOG_UI, "View " + view.getClass().getSimpleName() + " detached from " + this.getClass().getSimpleName());
        view = null;
    }

    @Override
    public void stop() {
        Log.i(LOG_UI, "Stopping " + this.getClass().getSimpleName());
        compositeDisposable.clear();
    }

    protected T getView() {
        return view;
    }

    protected boolean isViewAttached() {
        return view != null;
    }

    protected void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }
}
