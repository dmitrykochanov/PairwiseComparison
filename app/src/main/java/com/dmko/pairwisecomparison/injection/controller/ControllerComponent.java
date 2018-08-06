package com.dmko.pairwisecomparison.injection.controller;


import com.dmko.pairwisecomparison.injection.scopes.ControllerScope;
import com.dmko.pairwisecomparison.ui.screens.addeditcomparison.AddEditComparisonDialog;
import com.dmko.pairwisecomparison.ui.screens.addeditoption.AddEditOptionDialog;
import com.dmko.pairwisecomparison.ui.screens.comparison.ComparisonActivity;
import com.dmko.pairwisecomparison.ui.screens.comparison.comparisonresult.ComparisonResultFragment;
import com.dmko.pairwisecomparison.ui.screens.comparison.optioncomparisons.OptionComparisonsFragment;
import com.dmko.pairwisecomparison.ui.screens.comparison.options.OptionsFragment;
import com.dmko.pairwisecomparison.ui.screens.comparisons.ComparisonsFragment;
import com.dmko.pairwisecomparison.ui.screens.pasteoptions.PasteOptionsDialog;
import com.dmko.pairwisecomparison.ui.screens.recompare.RecompareFragment;

import dagger.Subcomponent;

@ControllerScope
@Subcomponent(modules = {ControllerModule.class, PresenterModule.class, InteractorModule.class})
public interface ControllerComponent {

    void inject(ComparisonsFragment comparisonsFragment);

    void inject(AddEditComparisonDialog addEditComparisonDialog);

    void inject(ComparisonResultFragment comparisonResultFragment);

    void inject(OptionsFragment optionsFragment);

    void inject(OptionComparisonsFragment optionComparisonsFragment);

    void inject(AddEditOptionDialog addEditOptionDialog);

    void inject(PasteOptionsDialog pasteOptionsDialog);

    void inject(RecompareFragment recompareFragment);

    void inject(ComparisonActivity comparisonActivity);
}
