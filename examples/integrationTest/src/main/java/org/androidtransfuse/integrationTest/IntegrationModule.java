package org.androidtransfuse.integrationTest;

import org.androidtransfuse.annotations.Bind;
import org.androidtransfuse.annotations.BindInterceptor;
import org.androidtransfuse.annotations.BindProvider;
import org.androidtransfuse.annotations.TransfuseModule;
import org.androidtransfuse.event.EventManager;
import org.androidtransfuse.event.EventManagerProvider;
import org.androidtransfuse.integrationTest.aop.AOPInterceptor;
import org.androidtransfuse.integrationTest.aop.DependencyInterceptor;
import org.androidtransfuse.integrationTest.aop.InjectedInterceptor;
import org.androidtransfuse.integrationTest.aop.InterceptorRecorder;
import org.androidtransfuse.integrationTest.inject.InjectTargetProvider;
import org.androidtransfuse.integrationTest.inject.LoopThree;
import org.androidtransfuse.integrationTest.inject.LoopThreeImpl;
import org.androidtransfuse.integrationTest.inject.ProvidedInjectTarget;
import org.androidtransfuse.integrationTest.layout.RandomProvider;

import java.util.Random;

/**
 * @author John Ericksen
 */
@TransfuseModule
public interface IntegrationModule {

    @Bind(LoopThreeImpl.class)
    LoopThree getThree();

    @BindInterceptor(AOPInterceptor.class)
    InterceptorRecorder getInterceptor();

    @BindInterceptor(DependencyInterceptor.class)
    InjectedInterceptor getDependencyInterceptor();

    @BindProvider(InjectTargetProvider.class)
    ProvidedInjectTarget getInjectTarget();

    @BindProvider(RandomProvider.class)
    Random getRandom();

    @BindProvider(EventManagerProvider.class)
    EventManager getEventManager();
}
