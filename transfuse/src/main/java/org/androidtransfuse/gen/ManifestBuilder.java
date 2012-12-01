package org.androidtransfuse.gen;

import org.androidtransfuse.model.manifest.Application;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class ManifestBuilder {

    private final Provider<Application> applicationProvider;

    @Inject
    public ManifestBuilder(Provider<Application> applicationProvider) {
        this.applicationProvider = applicationProvider;
    }

    public Application setupManifestApplication(String applicationClassName) {
        org.androidtransfuse.model.manifest.Application manifestApplication = applicationProvider.get();

        manifestApplication.setName(applicationClassName);

        return manifestApplication;
    }
}
