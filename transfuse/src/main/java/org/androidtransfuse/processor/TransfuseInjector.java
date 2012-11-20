package org.androidtransfuse.processor;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sun.codemodel.JCodeModel;
import org.androidtransfuse.config.TransfuseGenerateGuiceModule;
import org.androidtransfuse.config.TransfuseSetupGuiceModule;
import org.androidtransfuse.model.manifest.Manifest;
import org.androidtransfuse.model.r.RResource;
import org.androidtransfuse.util.MessagerLogger;

import javax.annotation.processing.ProcessingEnvironment;

/**
 * @author John Ericksen
 */
public final class TransfuseInjector {

    private static final TransfuseInjector INSTANCE = new TransfuseInjector();
    private Injector setupInjector = null;

    private TransfuseInjector() {
        //private singleton constructor
    }

    public static TransfuseInjector getInstance() {
        return INSTANCE;
    }

    public Injector buildSetupInjector(ProcessingEnvironment environment) {
        setupInjector = Guice.createInjector(new TransfuseSetupGuiceModule(
                new MessagerLogger(environment.getMessager()), environment.getFiler(), environment.getElementUtils()));
        return setupInjector;
    }

    public Injector buildProcessingInjector(RResource rResource, Manifest manifest, JCodeModel codeModel) {
        return setupInjector.createChildInjector(new TransfuseGenerateGuiceModule(rResource, manifest, codeModel));
    }

    public Injector buildInjector(final JCodeModel codeModel) {
        return setupInjector.createChildInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(JCodeModel.class).toInstance(codeModel);
            }
        });
    }
}
