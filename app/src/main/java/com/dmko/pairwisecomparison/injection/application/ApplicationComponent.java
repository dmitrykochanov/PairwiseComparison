package com.dmko.pairwisecomparison.injection.application;


import com.dmko.pairwisecomparison.injection.controller.ControllerComponent;
import com.dmko.pairwisecomparison.injection.controller.ControllerModule;
import com.dmko.pairwisecomparison.injection.controller.PresenterModule;
import com.dmko.pairwisecomparison.injection.scopes.ApplicationScope;

import dagger.Component;

@ApplicationScope
@Component(modules = {ApplicationModule.class, DatabaseModule.class, RepositoryModule.class})
public interface ApplicationComponent {
    ControllerComponent newControllerComponent(PresenterModule presenterModule, ControllerModule controllerModule);
}
