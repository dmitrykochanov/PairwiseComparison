package com.dmko.pairwisecomparison.ui.screens.comparisons

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.dmko.pairwisecomparison.R
import com.dmko.pairwisecomparison.ui.base.mvp.impl.BaseActivity
import com.dmko.pairwisecomparison.ui.screens.licenses.LicensesDialog
import com.dmko.pairwisecomparison.ui.screens.settings.SettingsActivity
import kotlinx.android.synthetic.main.activity_comparisons.*

class ComparisonsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comparisons)

        setSupportActionBar(toolbar)

        val fm = supportFragmentManager
        if (fm.findFragmentById(R.id.layout_fragment_container) == null) {
            val fragment = ComparisonsFragment()

            fab_add.setOnClickListener { fragment.onFabAddClicked() }

            fm.beginTransaction()
                    .add(R.id.layout_fragment_container, fragment)
                    .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.item_licenses -> {
                showDialog(LicensesDialog())
                true
            }
            R.id.item_settings -> {
                startActivity(SettingsActivity.getIntent(this))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
