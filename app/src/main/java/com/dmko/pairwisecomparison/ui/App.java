package com.dmko.pairwisecomparison.ui;


import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import com.dmko.pairwisecomparison.BuildConfig;
import com.dmko.pairwisecomparison.injection.application.ApplicationComponent;
import com.dmko.pairwisecomparison.injection.application.ApplicationModule;
import com.dmko.pairwisecomparison.injection.application.DaggerApplicationComponent;

import static com.dmko.pairwisecomparison.utils.LogTags.LOG_UI;

public class App extends Application {
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        setupLogs();

        if (BuildConfig.DEBUG) {
            StrictMode.enableDefaults();
        }
    }

    public ApplicationComponent getApplicationComponent() {
        if (applicationComponent == null) {
            applicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return applicationComponent;
    }

    private void setupLogs() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                Log.i(LOG_UI, activity.getClass().getSimpleName() + " created");
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Log.i(LOG_UI, activity.getClass().getSimpleName() + " started");
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Log.i(LOG_UI, activity.getClass().getSimpleName() + " resumed");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Log.i(LOG_UI, activity.getClass().getSimpleName() + " paused");
            }

            @Override
            public void onActivityStopped(Activity activity) {
                Log.i(LOG_UI, activity.getClass().getSimpleName() + " stopped");
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
                Log.i(LOG_UI, activity.getClass().getSimpleName() + " saved");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.i(LOG_UI, activity.getClass().getSimpleName() + " destroyed");
            }
        });
    }
}
