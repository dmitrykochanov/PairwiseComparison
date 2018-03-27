package com.dmko.pairwisecomparison.utils;


import io.reactivex.Scheduler;

public interface SchedulersFacade {
    Scheduler io();

    Scheduler ui();
}
