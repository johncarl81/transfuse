package org.androidtransfuse.config;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.thoughtworks.xstream.XStream;
import org.androidtransfuse.analysis.adapter.ASTFactory;
import org.androidtransfuse.util.Logger;

import javax.annotation.processing.Filer;
import javax.lang.model.util.Elements;

/**
 * @author John Ericksen
 */
public class TransfuseSetupGuiceModule extends AbstractModule {

    private final Logger logger;
    private final Filer filer;
    private final Elements elements;

    public TransfuseSetupGuiceModule(Logger logger, Filer filer, Elements elementUtils) {
        this.logger = logger;
        this.filer = filer;
        this.elements = elementUtils;
    }

    @Override
    protected void configure() {
        FactoryModuleBuilder factoryModuleBuilder = new FactoryModuleBuilder();

        install(factoryModuleBuilder.build(ASTFactory.class));

        bind(Logger.class).toInstance(logger);
        bind(XStream.class).toProvider(XStreamProvider.class);
        bind(Filer.class).toInstance(filer);
        bind(Elements.class).toInstance(elements);
    }
}
