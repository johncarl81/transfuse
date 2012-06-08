package org.androidtransfuse.config;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.thoughtworks.xstream.XStream;
import org.androidtransfuse.analysis.adapter.ASTFactory;
import org.androidtransfuse.gen.CodeWriterFactory;
import org.androidtransfuse.util.Logger;
import org.androidtransfuse.util.ManifestLocatorFactory;

/**
 * @author John Ericksen
 */
public class TransfuseSetupGuiceModule extends AbstractModule {

    private Logger logger;

    public TransfuseSetupGuiceModule(Logger logger) {
        this.logger = logger;
    }

    @Override
    protected void configure() {
        FactoryModuleBuilder factoryModuleBuilder = new FactoryModuleBuilder();

        install(factoryModuleBuilder.build(ManifestLocatorFactory.class));
        install(factoryModuleBuilder.build(ASTFactory.class));
        install(factoryModuleBuilder.build(CodeWriterFactory.class));

        bind(Logger.class).toInstance(logger);
        bind(XStream.class).toProvider(XStreamProvider.class);
    }
}
