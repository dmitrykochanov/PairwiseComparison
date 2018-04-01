package com.dmko.pairwisecomparison.ui.views;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.SeekBar;

import com.dmko.pairwisecomparison.R;
import com.dmko.pairwisecomparison.data.entities.OptionComparisonEntry;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.dmko.pairwisecomparison.utils.LogTags.LOG_APP;

public class OptionComparisonView extends ConstraintLayout {
    private static final int MAX_PROGRESS = 100;

    @BindView(R.id.button_left) AppCompatButton buttonLeft;
    @BindView(R.id.button_right) AppCompatButton buttonRight;
    @BindView(R.id.seek_bar) CenteredSeekBar centeredSeekBar;

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener;

    public OptionComparisonView(Context context) {
        super(context);
        initViews(context);
    }

    public OptionComparisonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public OptionComparisonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    public void init(OptionComparisonEntry entry) {
        Timber.tag(LOG_APP);
        Timber.i("Binding %s to %s, %s", OptionComparisonEntry.class.getSimpleName(), OptionComparisonView.class.getSimpleName(), entry);

        buttonLeft.setText(entry.getFirstOption().getName());
        buttonRight.setText(entry.getSecondOption().getName());

        int max = MAX_PROGRESS * 2;
        int progress = entry.getProgress() + MAX_PROGRESS;
        centeredSeekBar.setMax(max);
        centeredSeekBar.setProgress(progress);
    }

    public void setOnProgressChangedListener(SeekBar.OnSeekBarChangeListener onProgressChangedListener) {
        onSeekBarChangeListener = onProgressChangedListener;
    }

    private void initViews(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_option_comparison, this);
        ButterKnife.bind(this);

        centeredSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                if (progress < centeredSeekBar.getMax() / 2) {

                    highlightButton(buttonLeft, false);
                    highlightButton(buttonRight, true);
                } else if (progress > centeredSeekBar.getMax() / 2) {

                    highlightButton(buttonRight, false);
                    highlightButton(buttonLeft, true);
                } else {

                    highlightButton(buttonLeft, true);
                    highlightButton(buttonRight, true);
                }

                if (onSeekBarChangeListener != null) {
                    int optionProgress = progress - centeredSeekBar.getMax() / 2;
                    onSeekBarChangeListener.onProgressChanged(seekBar, optionProgress, b);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        buttonLeft.setOnClickListener(v -> {
            if (centeredSeekBar.getProgress() != 0) {
                centeredSeekBar.setProgress(0);
            } else {
                centeredSeekBar.setProgress(MAX_PROGRESS);
            }
        });

        buttonRight.setOnClickListener(v -> {
            if (centeredSeekBar.getProgress() != MAX_PROGRESS * 2) {
                centeredSeekBar.setProgress(centeredSeekBar.getMax());
            } else {
                centeredSeekBar.setProgress(MAX_PROGRESS);
            }
        });
    }

    @SuppressLint("RestrictedApi")
    private void highlightButton(AppCompatButton button, boolean reset) {
        if (!reset) {
            button.setSupportBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        } else {
            button.setSupportBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorButtonDefault)));
        }
    }
}
