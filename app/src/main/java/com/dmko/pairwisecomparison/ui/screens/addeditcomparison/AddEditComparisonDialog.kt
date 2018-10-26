package com.dmko.pairwisecomparison.ui.screens.addeditcomparison

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.systemService
import com.dmko.pairwisecomparison.R
import com.dmko.pairwisecomparison.data.entities.Comparison
import com.dmko.pairwisecomparison.ui.base.mvp.impl.BaseDialogFragment
import kotlinx.android.synthetic.main.dialog_add_edit_comparison.*
import javax.inject.Inject

class AddEditComparisonDialog : BaseDialogFragment(), AddEditComparisonContract.View {

    @Inject lateinit var presenter: AddEditComparisonContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        controllerComponent.inject(this)
        presenter.attachView(this)

        val comparisonId = arguments?.getString(ARG_COMP_ID)
                ?: throw IllegalArgumentException("no arguments")
        presenter.setArgs(comparisonId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.dialog_add_edit_comparison, container, false)

        button_cancel.setOnClickListener { closeDialog() }
        button_ok.setOnClickListener { onOkClicked()
        }

        input_text_name.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onOkClicked()
            }
            true
        }

        context?.systemService<InputMethodManager>()?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        input_name.editText?.requestFocus()

        return view
    }

    private fun onOkClicked() {
        val newName = input_name.editText?.text.toString().trim()
        presenter.saveComparison(newName)
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

    override fun showLoading(isLoading: Boolean) {
        progress_loading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun setComparison(comparison: Comparison) {
        text_title.setText(R.string.title_edit_comparison)
        val name = comparison.name
        input_name.editText!!.setText(name)
        input_name.editText!!.setSelection(name.length)
    }

    override fun showEmptyNameError() {
        input_name.isErrorEnabled = true
        input_name.error = getString(R.string.error_empty_comparison_name)
    }

    override fun closeDialog() {
        context?.systemService<InputMethodManager>()?.hideSoftInputFromWindow(input_name?.editText?.windowToken, 0)
        dialog.cancel()
    }

    companion object {

        private const val ARG_COMP_ID = "comp_id"

        fun newInstance(comparisonId: String?): AddEditComparisonDialog {
            val args = Bundle()
            args.putString(ARG_COMP_ID, comparisonId)
            val fragment = AddEditComparisonDialog()
            fragment.arguments = args
            return fragment
        }
    }
}