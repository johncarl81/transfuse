/**
 * Copyright 2011-2015 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidtransfuse.processor;

import org.androidtransfuse.config.TransfuseAndroidModule;
import org.androidtransfuse.model.manifest.Application;
import org.androidtransfuse.model.manifest.Manifest;
import org.androidtransfuse.transaction.AbstractCompletionTransactionWorker;
import org.androidtransfuse.util.Logger;
import org.androidtransfuse.util.ManifestSerializer;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.util.Collections;

/**
 * @author John Ericksen
 */
public class GenerateModuleProcessor extends AbstractCompletionTransactionWorker<Void, Void> {

    public static final String MANIFEST_PROCESSING_OPTION = "transfuseManifestProcessing";

    private final ManifestManager manifestManager;
    private final Merger merger;
    private final Manifest originalManifest;
    private final Logger log;
    private final File manifestFile;
    private ManifestSerializer manifestParser;
    private final String manifestProcessingOption;

    @Inject
    public GenerateModuleProcessor(ManifestManager manifestManager,
                                   Merger merger,
                                   @Named(TransfuseAndroidModule.ORIGINAL_MANIFEST)
                                   Manifest originalManifest,
                                   Logger log,
                                   @Named(TransfuseAndroidModule.MANIFEST_FILE)
                                   File manifestFile,
                                   ManifestSerializer manifestParser,
                                   @Named(MANIFEST_PROCESSING_OPTION)
                                   String manifestProcessingOption) {
        this.manifestManager = manifestManager;
        this.merger = merger;
        this.originalManifest = originalManifest;
        this.log = log;
        this.manifestFile = manifestFile;
        this.manifestParser = manifestParser;
        this.manifestProcessingOption = manifestProcessingOption;
    }

    @Override
    public Void innerRun(Void value) {

        //assembling generated code
        Manifest updatedManifest = buildManifest();

        if(!"off".equals(manifestProcessingOption)){
            if("log".equals(manifestProcessingOption)){
                log.info("Manifest generation piped to log");
                manifestParser.writeManifest(updatedManifest, System.out);
            }
            else{
                //write manifest back out, updating from processed classes
                manifestParser.writeManifest(updatedManifest, manifestFile);
            }
        }
        else{
            log.info("Manifest generation disabled");
        }

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

            return mergedManifest;
        } catch (MergerException e) {
            log.error("InstantiationException while merging manifest", e);
            return originalManifest;
        }
    }
}
