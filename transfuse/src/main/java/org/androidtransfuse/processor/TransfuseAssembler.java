package org.androidtransfuse.processor;

import org.androidtransfuse.config.TransfuseGenerateGuiceModule;
import org.androidtransfuse.model.manifest.Application;
import org.androidtransfuse.model.manifest.Manifest;
import org.androidtransfuse.util.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collections;

/**
 * @author John Ericksen
 */
public class TransfuseAssembler {

    private final Logger logger;
    private final Merger merger;
    private final Manifest originalManifest;
    private final ManifestManager manifestManager;

    @Inject
    public TransfuseAssembler(Logger logger, Merger merger,
                              @Named(TransfuseGenerateGuiceModule.ORIGINAL_MANIFEST) Manifest originalManifest,
                              ManifestManager manifestManager) {
        this.logger = logger;
        this.merger = merger;
        this.originalManifest = originalManifest;
        this.manifestManager = manifestManager;
    }

    public Manifest buildManifest() {
        try {

            Manifest manifest = manifestManager.getManifest();
            Manifest mergedManifest = merger.merge(Manifest.class, originalManifest, manifest);

            for (Application application : mergedManifest.getApplications()) {
                Collections.sort(application.getActivities());
            }

            mergedManifest.updatePackages();

            mergedManifest.updateNamespace();

            return mergedManifest;
        } catch (MergerException e) {
            logger.error("InstantiationException while merging manifest", e);
            return originalManifest;
        }
    }
}
