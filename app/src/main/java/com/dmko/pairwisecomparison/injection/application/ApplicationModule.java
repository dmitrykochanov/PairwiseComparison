package com.dmko.pairwisecomparison.injection.application;


import android.app.Application;
import android.content.Context;

import com.dmko.pairwisecomparison.injection.scopes.ApplicationScope;
import com.dmko.pairwisecomparison.utils.SchedulersFacade;
import com.dmko.pairwisecomparison.utils.SchedulersFacadeImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    private final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    @ApplicationScope
    public Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationScope
    public Context provideApplicationContext(Application application) {
        return application.getApplicationContext();
    }

    @Provides
    @ApplicationScope
    public SchedulersFacade provideSchedulersFacade() {
        return new SchedulersFacadeImpl();
    }
}
