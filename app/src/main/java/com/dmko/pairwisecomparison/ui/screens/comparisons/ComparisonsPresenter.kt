package com.dmko.pairwisecomparison.ui.screens.comparisons

import com.dmko.pairwisecomparison.data.entities.Comparison
import com.dmko.pairwisecomparison.data.repositories.ComparisonRepository
import com.dmko.pairwisecomparison.ui.base.mvp.impl.BasePresenterImpl
import com.dmko.pairwisecomparison.utils.LogTags.LOG_APP
import com.dmko.pairwisecomparison.utils.SchedulersFacade
import timber.log.Timber

class ComparisonsPresenter(private val schedulers: SchedulersFacade, private val repository: ComparisonRepository) : BasePresenterImpl<ComparisonsContract.View>(), ComparisonsContract.Presenter {

    override fun start() {
        Timber.tag(LOG_APP)
        Timber.i("Starting %s", this.javaClass.simpleName)

        view.showLoading(true)

        addDisposable(repository.comparisons
                .doOnNext { it.sort() }
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui())
                .subscribe { comparisons ->
                    Timber.tag(LOG_APP)
                    Timber.i("%s sending to %s, %s[%d]", this.javaClass.simpleName, view.javaClass.simpleName, Comparison::class.java.simpleName, comparisons.size)

                    if (comparisons.isEmpty()) {
                        view?.setEmptyComparisons()
                    } else {
                        view?.setComparisons(comparisons)
                    }
                    view?.showLoading(false)
                })
    }

    override fun openComparisonSelected(comparison: Comparison) {
        view?.openComparison(comparison)
    }

    override fun addComparisonSelected() {
        view?.showAddEditComparisonDialog(null)
    }

    override fun updateComparisonSelected(comparison: Comparison) {
        view?.showAddEditComparisonDialog(comparison.id)
    }

    override fun deleteComparisonSelected(comparison: Comparison) {
        addDisposable(repository.deleteComparison(comparison)
                .subscribeOn(schedulers.io())
                .subscribe())
    }

    override fun copyComparisonSelected(comparison: Comparison) {
        view?.openCopyComparisonDialog(comparison.id)
    }
}
