package org.androidrobotics.config;

import com.google.inject.AbstractModule;
import com.sun.codemodel.JCodeModel;

/**
 * @author John Ericksen
 */
public class RoboticsGenerationGuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(JCodeModel.class).asEagerSingleton();
    }
}
