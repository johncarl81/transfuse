package org.androidrobotics.example.simple;

import org.androidrobotics.annotations.Bind;
import org.androidrobotics.annotations.BindInterceptor;
import org.androidrobotics.annotations.BindProvider;
import org.androidrobotics.annotations.RoboticsModule;

/**
 * @author John Ericksen
 */
@RoboticsModule
public interface SimpleModule {

    @Bind(AnotherValueImpl.class)
    AnotherValue bindAnotherValue();

    @BindInterceptor(LogInterception.class)
    LoggingInterceptor bindInterceptor();

    @BindProvider(ValueProvider.class)
    ProvidedValue bindValueProvider();
}