package org.androidtransfuse.config;

import org.androidtransfuse.annotations.BindProvider;
import org.androidtransfuse.annotations.TransfuseModule;
import org.androidtransfuse.event.EventManager;
import org.androidtransfuse.event.EventManagerProvider;

//todo:better configuration of base configuration
@TransfuseModule
public interface TransfuseAndroidModule {

    @BindProvider(EventManagerProvider.class)
    EventManager getEventManager();
}