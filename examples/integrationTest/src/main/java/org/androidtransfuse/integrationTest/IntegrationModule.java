package org.androidtransfuse.integrationTest;

import org.androidtransfuse.annotations.Bind;
import org.androidtransfuse.annotations.BindInterceptor;
import org.androidtransfuse.annotations.BindProvider;
import org.androidtransfuse.annotations.TransfuseModule;
import org.androidtransfuse.integrationTest.aop.AOPInterceptor;
import org.androidtransfuse.integrationTest.aop.DependencyInterceptor;
import org.androidtransfuse.integrationTest.aop.InjectedInterceptor;
import org.androidtransfuse.integrationTest.aop.InterceptorRecorder;
import org.androidtransfuse.integrationTest.externalGenerator.Proxied;
import org.androidtransfuse.integrationTest.externalGenerator.ProxiedProxy;
import org.androidtransfuse.integrationTest.inject.*;
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

    @Bind(ConcreteType.class)
    GenericType<Concrete> getTarget();

    @Bind(Concrete2Type.class)
    GenericType<Concrete2> getTarget2();

    @Bind(ProxiedProxy.class)
    Proxied getProxied();
}
