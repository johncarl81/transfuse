package org.androidtransfuse.processor;

import com.sun.codemodel.CodeWriter;
import com.sun.codemodel.JCodeModel;
import org.androidtransfuse.config.TransfuseGenerateGuiceModule;
import org.androidtransfuse.model.manifest.Application;
import org.androidtransfuse.model.manifest.Manifest;
import org.androidtransfuse.util.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.Collections;

/**
 * @author John Ericksen
 */
public class TransfuseAssembler {

    private JCodeModel codeModel;
    private Logger logger;
    private Merger merger;
    private Manifest originalManifest;
    private ManifestManager manifestManager;

    @Inject
    public TransfuseAssembler(JCodeModel codeModel, Logger logger, Merger merger,
                              @Named(TransfuseGenerateGuiceModule.ORIGINAL_MANIFEST) Manifest originalManifest,
                              ManifestManager manifestManager) {
        this.codeModel = codeModel;
        this.logger = logger;
        this.merger = merger;
        this.originalManifest = originalManifest;
        this.manifestManager = manifestManager;
    }

    public void writeSource(CodeWriter codeWriter, CodeWriter resourceWriter) {

        try {
            codeModel.build(
                    codeWriter,
                    resourceWriter);

        } catch (IOException e) {
            logger.error("IOException while writing source files", e);
        }
    }

    public Manifest buildManifest() {

        Manifest manifest = manifestManager.getManifest();

        try {
            Manifest mergedManifest = merger.merge(Manifest.class, originalManifest, manifest);

            for (Application application : mergedManifest.getApplications()) {
                Collections.sort(application.getActivities());
            }

            return mergedManifest;
        } catch (MergerException e) {
            logger.error("InstantiationException while merging manifest", e);
            return originalManifest;
        }
    }
}
