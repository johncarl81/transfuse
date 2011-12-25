package org.androidrobotics.example.simple;

import org.androidrobotics.annotations.Bind;
import org.androidrobotics.annotations.BindInterceptor;
import org.androidrobotics.annotations.RoboticsModule;

/**
 * @author John Ericksen
 */
@RoboticsModule
public interface SimpleModule {

    @Bind(AnotherValueImpl.class)
    AnotherValue binding(AnotherValueImpl temp);

    @BindInterceptor
    LoggingInterceptor binding(LogInterception interception);
}