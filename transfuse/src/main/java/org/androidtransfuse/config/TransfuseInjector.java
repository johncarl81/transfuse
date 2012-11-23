package org.androidtransfuse.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
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
                new MessagerLogger(
                        environment.getMessager()),
                environment.getFiler(),
                environment.getElementUtils(),
                new ThreadLocalScope()));
        return setupInjector;
    }

    public Injector buildProcessingInjector(RResource rResource, Manifest manifest) {
        return setupInjector.createChildInjector(new TransfuseGenerateGuiceModule(rResource, manifest));
    }
}
