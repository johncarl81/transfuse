package org.androidtransfuse.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.androidtransfuse.util.MessagerLogger;

import javax.annotation.processing.ProcessingEnvironment;

/**
 * @author John Ericksen
 */
public final class TransfuseInjector {

    private TransfuseInjector() {
        //private singleton constructor
    }

    public static Injector buildInjector(ProcessingEnvironment environment) {
        return Guice.createInjector(new TransfuseSetupGuiceModule(
                new MessagerLogger(environment.getMessager()),
                new SynchronizedFiler(environment.getFiler()),
                new SynchronizedElements(environment.getElementUtils()),
                new ThreadLocalScope()),
                new TransfuseGenerateGuiceModule(new MapScope()));
    }
}
