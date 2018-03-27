package com.dmko.pairwisecomparison.ui.views;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;

import com.dmko.pairwisecomparison.R;

public class CenteredSeekBar extends AppCompatSeekBar {
    private int progressColor = getResources().getColor(R.color.colorAccent);
    private int backgroundColor = Color.GRAY;

    private Rect rect;
    private Paint paint;
    private int seekBarHeight;

    public CenteredSeekBar(Context context) {
        super(context);
    }

    public CenteredSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        rect = new Rect();
        paint = new Paint();
        seekBarHeight = 6;
    }

    public CenteredSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        rect.set(getThumbOffset(),
                (getHeight() / 2) - (seekBarHeight / 2),
                getWidth() - getThumbOffset(),
                (getHeight() / 2) + (seekBarHeight / 2));

        paint.setColor(backgroundColor);

        canvas.drawRect(rect, paint);

        if (this.getProgress() > getMax() / 2) {

            rect.set(getWidth() / 2,
                    (getHeight() / 2) - (seekBarHeight / 2),
                    getWidth() / 2 + (getWidth() / getMax()) * (getProgress() - getMax() / 2),
                    getHeight() / 2 + (seekBarHeight / 2));

            paint.setColor(progressColor);
            canvas.drawRect(rect, paint);
        }

        if (this.getProgress() < getMax() / 2) {

            rect.set(getWidth() / 2 - ((getWidth() / getMax()) * (getMax() / 2 - getProgress())),
                    (getHeight() / 2) - (seekBarHeight / 2),
                    getWidth() / 2,
                    getHeight() / 2 + (seekBarHeight / 2));

            paint.setColor(progressColor);
            canvas.drawRect(rect, paint);
        }
    }
}
