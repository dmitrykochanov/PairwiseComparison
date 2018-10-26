package com.dmko.pairwisecomparison.ui.screens.comparisons

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dmko.pairwisecomparison.R
import com.dmko.pairwisecomparison.data.entities.Comparison
import com.dmko.pairwisecomparison.ui.base.mvp.impl.BaseFragment
import com.dmko.pairwisecomparison.ui.screens.addeditcomparison.AddEditComparisonDialog
import com.dmko.pairwisecomparison.ui.screens.comparison.ComparisonActivity
import com.dmko.pairwisecomparison.ui.screens.comparisons.recyclerview.ComparisonsAdapter
import com.dmko.pairwisecomparison.ui.screens.copycomparison.CopyComparisonDialog
import kotlinx.android.synthetic.main.fragment_comparisons.*
import javax.inject.Inject

class ComparisonsFragment : BaseFragment(), ComparisonsContract.View {

    @Inject lateinit var presenter: ComparisonsContract.Presenter
    @Inject lateinit var adapter: ComparisonsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        controllerComponent.inject(this)
        presenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_comparisons, container, false)
        setupRecyclerView()
        return view
    }

    override fun onStart() {
        super.onStart()
        presenter.start()
    }

    override fun onStop() {
        super.onStop()
        presenter.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun setComparisons(comparisons: List<Comparison>) {
        adapter.setComparisons(comparisons)
        recycler_comparisons.visibility = View.VISIBLE
        text_empty_title.visibility = View.GONE
        text_empty_description.visibility = View.GONE
    }

    override fun setEmptyComparisons() {
        recycler_comparisons.visibility = View.GONE
        text_empty_title.visibility = View.VISIBLE
        text_empty_description.visibility = View.VISIBLE
    }

    override fun showLoading(isLoading: Boolean) {
        progress_loading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun showAddEditComparisonDialog(comparisonId: String?) {
        val dialog = AddEditComparisonDialog.newInstance(comparisonId)
        showDialog(dialog)
    }

    override fun openComparison(comparison: Comparison) {
        val intent = ComparisonActivity.getIntent(context, comparison.id, comparison.name)
        startActivity(intent)
    }

    override fun openCopyComparisonDialog(comparisonId: String) {
        val dialog = CopyComparisonDialog.newInstance(comparisonId)
        showDialog(dialog)
    }

    fun onFabAddClicked() {
        presenter.addComparisonSelected()
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        recycler_comparisons.layoutManager = layoutManager
        recycler_comparisons.adapter = adapter

        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        recycler_comparisons.addItemDecoration(itemDecoration)
    }
}