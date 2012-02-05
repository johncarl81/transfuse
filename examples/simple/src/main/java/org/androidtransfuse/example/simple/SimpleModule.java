package org.androidtransfuse.example.simple;

import org.androidtransfuse.annotations.Bind;
import org.androidtransfuse.annotations.BindInterceptor;
import org.androidtransfuse.annotations.BindProvider;
import org.androidtransfuse.annotations.TransfuseModule;

/**
 * @author John Ericksen
 */
@TransfuseModule
public interface SimpleModule {

    @Bind(AnotherValueImpl.class)
    AnotherValue bindAnotherValue();

    @BindInterceptor(LogInterception.class)
    LoggingInterceptor bindInterceptor();

    @BindProvider(ValueProvider.class)
    ProvidedValue bindValueProvider();

    @BindProvider(SingletonProvider.class)
    SingletonTarget bindSingletonTarget();

    @BindProvider(ProviderTest.class)
    ProviderTestValue bindProviderTest();
}