package org.androidtransfuse;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import org.androidtransfuse.config.TransfuseGenerateGuiceModule;
import org.androidtransfuse.config.TransfuseSetupGuiceModule;
import org.androidtransfuse.model.manifest.Application;
import org.androidtransfuse.model.manifest.Manifest;
import org.androidtransfuse.util.EmptyRResource;
import org.androidtransfuse.util.JavaUtilLogger;

/**
 * @author John Ericksen
 */
public class TransfuseTestInjector {

    public static void inject(Object input) {
        Injector injector = getInjector(input);
        injector.injectMembers(input);
    }

    public static Injector getInjector(Object input) {
        Manifest manifest = new Manifest();

        manifest.getApplications().add(new Application());

        return Guice.createInjector(Stage.DEVELOPMENT,
                new TransfuseSetupGuiceModule(new JavaUtilLogger(input), new NoOpFiler()),
                new TransfuseGenerateGuiceModule(new EmptyRResource(), manifest));
    }
}
