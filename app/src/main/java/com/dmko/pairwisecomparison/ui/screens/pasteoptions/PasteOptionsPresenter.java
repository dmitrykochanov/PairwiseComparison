package com.dmko.pairwisecomparison.ui.screens.pasteoptions;

import com.dmko.pairwisecomparison.data.entities.Option;
import com.dmko.pairwisecomparison.data.repositories.OptionsRepository;
import com.dmko.pairwisecomparison.ui.base.mvp.impl.BasePresenterImpl;
import com.dmko.pairwisecomparison.utils.SchedulersFacade;

import java.util.ArrayList;
import java.util.List;

public class PasteOptionsPresenter extends BasePresenterImpl<PasteOptionsContract.View> implements PasteOptionsContract.Presenter {

    private SchedulersFacade schedulers;
    private OptionsRepository optionsRepository;
    private List<Option> options = new ArrayList<>();

    public PasteOptionsPresenter(SchedulersFacade schedulers, OptionsRepository optionsRepository) {
        this.schedulers = schedulers;
        this.optionsRepository = optionsRepository;
    }

    @Override
    public void start(String comparisonId, String clipboardContent) {
        parseClipBoardContent(comparisonId, clipboardContent);
        getView().setOptions(options);
    }

    @Override
    public void updateOption(Option option) {
        for (Option o : options) {
            if (o.getId().equals(option.getId())) {
                o.setName(option.getName());
                break;
            }
        }
    }

    @Override
    public void deleteOption(Option option) {
        options.remove(option);
        getView().setOptions(options);
    }

    @Override
    public void saveOptions() {
        addDisposable(optionsRepository.insertOptions(options)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui())
                .subscribe(() -> {
                    if (isViewAttached()) {
                        getView().closeDialog();
                    }
                }));
    }

    private void parseClipBoardContent(String comparisonId, String clipboardContent) {
        String[] optionNames = clipboardContent.split("\n");
        for (String optionName : optionNames) {
            Option option = new Option();
            option.setComparisonId(comparisonId);
            option.setName(optionName);
            options.add(option);
        }
    }
}
