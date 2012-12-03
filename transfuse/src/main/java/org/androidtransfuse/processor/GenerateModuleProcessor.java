package org.androidtransfuse.processor;

import org.androidtransfuse.config.TransfuseGenerateGuiceModule;
import org.androidtransfuse.model.manifest.Application;
import org.androidtransfuse.model.manifest.Manifest;
import org.androidtransfuse.util.Logger;
import org.androidtransfuse.util.ManifestSerializer;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.util.Collections;

/**
 * @author John Ericksen
 */
public class GenerateModuleProcessor implements TransactionWorker<Void, Void> {

    private final ManifestManager manifestManager;
    private final Merger merger;
    private final Manifest originalManifest;
    private final Logger logger;
    private final File manifestFile;
    private ManifestSerializer manifestParser;
    private boolean complete = false;

    @Inject
    public GenerateModuleProcessor(ManifestManager manifestManager,
                                   Merger merger,
                                   @Named(TransfuseGenerateGuiceModule.ORIGINAL_MANIFEST)
                                   Manifest originalManifest,
                                   Logger logger,
                                   @Named(TransfuseGenerateGuiceModule.MANIFEST_FILE) File manifestFile,
                                   ManifestSerializer manifestParser) {
        this.manifestManager = manifestManager;
        this.merger = merger;
        this.originalManifest = originalManifest;
        this.logger = logger;
        this.manifestFile = manifestFile;
        this.manifestParser = manifestParser;
    }

    @Override
    public boolean isComplete() {
        return complete;
    }

    @Override
    public Void run(Void value) {

        //assembling generated code
        Manifest updatedManifest = buildManifest();

        //write manifest back out, updating from processed classes
        manifestParser.writeManifest(updatedManifest, manifestFile);

        complete = true;
        return null;
    }

    private Manifest buildManifest() {
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

    @Override
    public Exception getError() {
        return null;
    }
}
