package com.dmko.pairwisecomparison.ui.views;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;

public class ClearFocusEditText extends TextInputEditText {

    public ClearFocusEditText(Context context) {
        super(context);
    }

    public ClearFocusEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClearFocusEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            clearFocus();
        }
        return super.onKeyPreIme(keyCode, event);
    }
}
