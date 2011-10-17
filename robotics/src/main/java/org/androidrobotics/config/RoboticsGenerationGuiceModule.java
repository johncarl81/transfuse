package org.androidrobotics.config;

import com.google.inject.AbstractModule;
import com.sun.codemodel.JCodeModel;
import org.androidrobotics.util.Logger;

/**
 * @author John Ericksen
 */
public class RoboticsGenerationGuiceModule extends AbstractModule {

    private Logger logger;

    public RoboticsGenerationGuiceModule(Logger logger) {
        this.logger = logger;
    }

    @Override
    protected void configure() {
        bind(JCodeModel.class).asEagerSingleton();

        bind(Logger.class).toInstance(logger);
    }
}
