package org.androidrobotics.example.simple;

import org.androidrobotics.annotations.ImplementedBy;
import org.androidrobotics.annotations.RoboticsModule;

/**
 * @author John Ericksen
 */
@RoboticsModule
public interface SimpleModule {

    @ImplementedBy
    AnotherValue providedBy(AnotherValueImpl input);
}