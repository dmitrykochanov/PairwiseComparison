package com.dmko.pairwisecomparison.ui.screens.addeditcomparison

import com.dmko.pairwisecomparison.data.entities.Comparison
import com.dmko.pairwisecomparison.data.repositories.ComparisonRepository
import com.dmko.pairwisecomparison.ui.base.mvp.impl.BasePresenterImpl
import com.dmko.pairwisecomparison.utils.LogTags.LOG_APP
import com.dmko.pairwisecomparison.utils.SchedulersFacade
import timber.log.Timber

class AddEditComparisonPresenter(private val schedulers: SchedulersFacade, private val repository: ComparisonRepository) : BasePresenterImpl<AddEditComparisonContract.View>(), AddEditComparisonContract.Presenter {

    private var comparisonId: String? = null
    private lateinit var comparison: Comparison

    override fun setArgs(comparisonId: String?) {
        this.comparisonId = comparisonId
    }

    override fun start() {
        Timber.tag(LOG_APP)
        Timber.i("Starting %s with comparisonId = %s", this.javaClass.simpleName, comparisonId)

        view?.showLoading(true)
        if (comparisonId == null) {
            comparison = Comparison()
            view?.showLoading(false)
        } else {
            addDisposable(repository.getComparisonById(comparisonId)
                    .subscribeOn(schedulers.io())
                    .observeOn(schedulers.ui())
                    .subscribe { comparison ->
                        Timber.tag(LOG_APP)
                        Timber.i("%s sending to %s, %s", this.javaClass.simpleName, view.javaClass.simpleName, comparison)

                        this.comparison = comparison
                        view?.setComparison(comparison)
                        view?.showLoading(false)
                    })
        }
    }

    override fun saveComparison(comparisonName: String) {
        if (comparisonName.isEmpty()) {
            view?.showEmptyNameError()
        } else {
            comparison.name = comparisonName
            if (comparisonId == null) {
                addDisposable(repository.insertComparison(comparison)
                        .subscribeOn(schedulers.io())
                        .subscribe {
                            view?.closeDialog()
                        })
            } else {
                addDisposable(repository.updateComparison(comparison)
                        .subscribeOn(schedulers.io())
                        .subscribe {
                            view?.closeDialog()
                        })
            }
        }
    }
}
