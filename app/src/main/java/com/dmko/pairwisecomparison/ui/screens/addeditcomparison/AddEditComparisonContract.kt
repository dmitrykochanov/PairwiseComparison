package com.dmko.pairwisecomparison.ui.screens.addeditcomparison

import com.dmko.pairwisecomparison.data.entities.Comparison
import com.dmko.pairwisecomparison.ui.base.mvp.BasePresenter
import com.dmko.pairwisecomparison.ui.base.mvp.BaseView

interface AddEditComparisonContract {

    interface View : BaseView {

        fun showLoading(isLoading: Boolean)

        fun setComparison(comparison: Comparison)

        fun showEmptyNameError()

        fun closeDialog()
    }

    interface Presenter : BasePresenter<View> {

        fun setArgs(comparisonId: String?)

        fun start()

        fun saveComparison(comparisonName: String)
    }
}