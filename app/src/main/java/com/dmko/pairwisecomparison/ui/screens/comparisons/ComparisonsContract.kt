package com.dmko.pairwisecomparison.ui.screens.comparisons

import com.dmko.pairwisecomparison.data.entities.Comparison
import com.dmko.pairwisecomparison.ui.base.mvp.BasePresenter
import com.dmko.pairwisecomparison.ui.base.mvp.BaseView

interface ComparisonsContract {

    interface View : BaseView {

        fun showLoading(isLoading: Boolean)

        fun setComparisons(comparisons: List<Comparison>)

        fun setEmptyComparisons()

        fun showAddEditComparisonDialog(comparisonId: String?)

        fun openComparison(comparison: Comparison)

        fun openCopyComparisonDialog(comparisonId: String)
    }

    interface Presenter : BasePresenter<View> {

        fun start()

        fun openComparisonSelected(comparison: Comparison)

        fun addComparisonSelected()

        fun updateComparisonSelected(comparison: Comparison)

        fun deleteComparisonSelected(comparison: Comparison)

        fun copyComparisonSelected(comparison: Comparison)
    }
}