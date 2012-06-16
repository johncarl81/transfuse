package org.androidtransfuse.processor;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.androidtransfuse.config.TransfuseGenerateGuiceModule;
import org.androidtransfuse.config.TransfuseSetupGuiceModule;
import org.androidtransfuse.model.manifest.Manifest;
import org.androidtransfuse.model.r.RResource;
import org.androidtransfuse.util.MessagerLogger;

import javax.annotation.processing.Messager;

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

    public Injector buildSetupInjector(Messager messager) {
        setupInjector = Guice.createInjector(new TransfuseSetupGuiceModule(new MessagerLogger(messager)));
        return setupInjector;
    }

    public Injector buildProcessingInjector(RResource rResource, Manifest manifest) {
        return setupInjector.createChildInjector(new TransfuseGenerateGuiceModule(rResource, manifest));
    }
}
