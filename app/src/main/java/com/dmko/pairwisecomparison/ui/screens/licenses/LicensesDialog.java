package com.dmko.pairwisecomparison.ui.screens.licenses;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.webkit.WebView;

import com.dmko.pairwisecomparison.R;

public class LicensesDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        WebView webView = (WebView) LayoutInflater.from(getActivity()).inflate(R.layout.dialog_licenses, null);
        webView.loadUrl("file:///android_asset/licenses.html");

        return new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Light_Dialog_Alert)
                .setTitle("Licenses")
                .setView(webView)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }
}
