package org.androidtransfuse.config;

import org.androidtransfuse.annotations.BindProvider;
import org.androidtransfuse.annotations.BindProviders;
import org.androidtransfuse.annotations.TransfuseModule;
import org.androidtransfuse.event.EventManager;
import org.androidtransfuse.event.EventManagerProvider;

@TransfuseModule
@BindProviders(
        @BindProvider(type = EventManager.class, provider = EventManagerProvider.class)
)
public class TransfuseAndroidModule {
}