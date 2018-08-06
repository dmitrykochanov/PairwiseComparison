package com.dmko.pairwisecomparison.injection.application;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.dmko.pairwisecomparison.injection.scopes.ApplicationScope;
import com.dmko.pairwisecomparison.utils.SchedulersFacade;
import com.dmko.pairwisecomparison.utils.SchedulersFacadeImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @ApplicationScope
    public Application provideApplication() {
        return application;
    }

    @Provides
    @ApplicationScope
    public Context provideApplicationContext(Application application) {
        return application.getApplicationContext();
    }

    @Provides
    @ApplicationScope
    public SharedPreferences provideSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    @ApplicationScope
    public Resources provideResources(Context context) {
        return context.getResources();
    }

    @Provides
    @ApplicationScope
    public SchedulersFacade provideSchedulersFacade() {
        return new SchedulersFacadeImpl();
    }
}
