package com.dmko.pairwisecomparison.ui.base.mvp.impl;


import com.dmko.pairwisecomparison.ui.base.mvp.BasePresenter;
import com.dmko.pairwisecomparison.ui.base.mvp.BaseView;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

import static com.dmko.pairwisecomparison.utils.LogTags.LOG_UI;

public class BasePresenterImpl<T extends BaseView> implements BasePresenter<T> {
    private T view;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public void attachView(T view) {
        this.view = view;
        Timber.tag(LOG_UI);
        Timber.i("View %s is attached to %s", view.getClass().getSimpleName(), this.getClass().getSimpleName());
    }

    @Override
    public void detachView() {
        Timber.tag(LOG_UI);
        Timber.i("View %s is detached from %s", view.getClass().getSimpleName(), this.getClass().getSimpleName());
        view = null;
    }

    @Override
    public void stop() {
        Timber.tag(LOG_UI);
        Timber.i("Stopping %s", this.getClass().getSimpleName());
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
