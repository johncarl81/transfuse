package org.androidrobotics;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.androidrobotics.config.RoboticsGenerationGuiceModule;
import org.androidrobotics.util.JavaUtilLogger;

import java.util.logging.Logger;

/**
 * @author John Ericksen
 */
public class TestInjectorBuilder {

    public static Injector createInjector(Object testInstance) {
        org.androidrobotics.util.Logger logger = new JavaUtilLogger(Logger.getLogger(testInstance.getClass().getName()));
        return Guice.createInjector(new RoboticsGenerationGuiceModule(logger));
    }
}
