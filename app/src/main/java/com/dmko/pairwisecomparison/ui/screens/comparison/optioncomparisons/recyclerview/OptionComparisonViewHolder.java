package com.dmko.pairwisecomparison.ui.screens.comparison.optioncomparisons.recyclerview;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SeekBar;

import com.dmko.pairwisecomparison.R;
import com.dmko.pairwisecomparison.data.entities.OptionComparisonEntry;
import com.dmko.pairwisecomparison.ui.screens.comparison.optioncomparisons.OptionComparisonsContract;
import com.dmko.pairwisecomparison.ui.views.OptionComparisonView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OptionComparisonViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.view_option_comparison) OptionComparisonView optionComparisonView;
    private OptionComparisonEntry optionComparison;

    public OptionComparisonViewHolder(View itemView, OptionComparisonsContract.Presenter presenter) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        optionComparisonView.setOnProgressChangedListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                if (optionComparison != null) {
                    optionComparison.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void bindOptionComparison(OptionComparisonEntry optionComparison) {
        this.optionComparison = optionComparison;
        optionComparisonView.init(optionComparison);
    }
}
