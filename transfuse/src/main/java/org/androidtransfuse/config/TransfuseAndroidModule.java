package org.androidtransfuse.config;

import org.androidtransfuse.annotations.BindProvider;
import org.androidtransfuse.annotations.Providers;
import org.androidtransfuse.annotations.TransfuseModule;
import org.androidtransfuse.event.EventManager;
import org.androidtransfuse.event.EventManagerProvider;

//todo:better configuration of base configuration
@TransfuseModule
@Providers(
        @BindProvider(type = EventManager.class, provider = EventManagerProvider.class)
)
public class TransfuseAndroidModule {
}