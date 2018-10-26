package com.dmko.pairwisecomparison.ui.screens.comparisons.recyclerview


import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.dmko.pairwisecomparison.R
import com.dmko.pairwisecomparison.data.entities.Comparison
import com.dmko.pairwisecomparison.ui.screens.comparisons.ComparisonsContract

class ComparisonViewHolder(itemView: View, presenter: ComparisonsContract.Presenter) : RecyclerView.ViewHolder(itemView) {

    @BindView(R.id.text_name) internal var textName: TextView? = null
    @BindView(R.id.image_edit) internal var imageEdit: ImageView? = null
    @BindView(R.id.image_delete) internal var imageDelete: ImageView? = null
    @BindView(R.id.image_copy) internal var imageCopy: ImageView? = null

    private var comparison: Comparison? = null

    init {
        ButterKnife.bind(this, itemView)

        itemView.setOnClickListener { presenter.openComparisonSelected(comparison!!) }

        imageCopy!!.setOnClickListener { presenter.copyComparisonSelected(comparison!!) }

        imageEdit!!.setOnClickListener { presenter.updateComparisonSelected(comparison!!) }

        imageDelete!!.setOnClickListener { presenter.deleteComparisonSelected(comparison!!) }
    }

    fun bindComparison(comparison: Comparison) {
        this.comparison = comparison
        textName!!.text = comparison.name
    }
}
