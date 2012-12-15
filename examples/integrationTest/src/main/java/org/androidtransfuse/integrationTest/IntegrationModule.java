package org.androidtransfuse.integrationTest;

import org.androidtransfuse.annotations.*;
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
@Interceptors({
        @BindInterceptor(annotation = AOPInterceptor.class, interceptor = InterceptorRecorder.class),
        @BindInterceptor(annotation = DependencyInterceptor.class, interceptor = InjectedInterceptor.class)
})
@Providers({
        @BindProvider(type = ProvidedInjectTarget.class, provider = InjectTargetProvider.class),
        @BindProvider(type = Random.class, provider = RandomProvider.class)
})
public interface IntegrationModule {

    @Bind(LoopThreeImpl.class)
    LoopThree getThree();

    @Bind(ConcreteType.class)
    GenericType<Concrete> getTarget();

    @Bind(Concrete2Type.class)
    GenericType<Concrete2> getTarget2();

    @Bind(ProxiedProxy.class)
    Proxied getProxied();
}
