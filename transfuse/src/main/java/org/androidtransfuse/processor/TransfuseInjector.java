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
public class TransfuseInjector {

    private static Injector setupInjector;

    public static Injector buildSetupInjector(Messager messager) {
        setupInjector = Guice.createInjector(new TransfuseSetupGuiceModule(new MessagerLogger(messager)));
        return setupInjector;
    }

    public static Injector buildProcessingInjector(RResource rResource, Manifest manifest) {
        return setupInjector.createChildInjector(new TransfuseGenerateGuiceModule(rResource, manifest));
    }
}
