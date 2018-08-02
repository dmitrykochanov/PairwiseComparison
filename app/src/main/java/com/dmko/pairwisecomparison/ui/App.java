package com.dmko.pairwisecomparison.ui;


import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.StrictMode;

import com.dmko.pairwisecomparison.BuildConfig;
import com.dmko.pairwisecomparison.injection.application.ApplicationComponent;
import com.dmko.pairwisecomparison.injection.application.ApplicationModule;
import com.dmko.pairwisecomparison.injection.application.DaggerApplicationComponent;
import com.dmko.pairwisecomparison.utils.ProductionTree;
import com.squareup.leakcanary.LeakCanary;

import timber.log.Timber;

import static com.dmko.pairwisecomparison.utils.LogTags.LOG_UI;

public class App extends Application {
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        if(LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);

        if (BuildConfig.DEBUG) {
            setupLogs();
            StrictMode.enableDefaults();
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new ProductionTree());
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
                Timber.tag(LOG_UI);
                Timber.i("%s created", activity.getClass().getSimpleName());
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Timber.tag(LOG_UI);
                Timber.i("%s started", activity.getClass().getSimpleName());
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Timber.tag(LOG_UI);
                Timber.i("%s resumed", activity.getClass().getSimpleName());
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Timber.tag(LOG_UI);
                Timber.i("%s paused", activity.getClass().getSimpleName());
            }

            @Override
            public void onActivityStopped(Activity activity) {
                Timber.tag(LOG_UI);
                Timber.i("%s stopped", activity.getClass().getSimpleName());
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
                Timber.tag(LOG_UI);
                Timber.i("%s saved", activity.getClass().getSimpleName());
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Timber.tag(LOG_UI);
                Timber.i("%s destroyed", activity.getClass().getSimpleName());
            }
        });
    }
}
