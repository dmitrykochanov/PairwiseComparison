package com.dmko.pairwisecomparison.ui.base.mvp;


public interface BasePresenter<T extends BaseView> {

    void attachView(T view);

    void detachView();

    void stop();
}
