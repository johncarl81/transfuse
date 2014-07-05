/**
 * Copyright 2013 John Ericksen
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
package org.androidtransfuse.experiment.generators;

import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.analysis.MetaDataBuilder;
import org.androidtransfuse.annotations.Application;
import org.androidtransfuse.annotations.UIOptions;
import org.androidtransfuse.experiment.ComponentBuilder;
import org.androidtransfuse.experiment.ComponentDescriptor;
import org.androidtransfuse.experiment.ComponentPartGenerator;
import org.androidtransfuse.experiment.PreInjectionGeneration;
import org.androidtransfuse.processor.ManifestManager;

import javax.inject.Inject;
import javax.inject.Provider;

import static org.androidtransfuse.util.AnnotationUtil.checkBlank;
import static org.androidtransfuse.util.AnnotationUtil.checkDefault;

/**
 * @author John Ericksen
 */
public class ApplicationManifestEntryGenerator implements PreInjectionGeneration {

    private final MetaDataBuilder metadataBuilder;
    private final ManifestManager manifestManager;
    private final Provider<org.androidtransfuse.model.manifest.Application> applicationProvider;

    @Inject
    public ApplicationManifestEntryGenerator(MetaDataBuilder metadataBuilder,
                                             ManifestManager manifestManager,
                                             Provider<org.androidtransfuse.model.manifest.Application> applicationProvider) {
        this.metadataBuilder = metadataBuilder;
        this.manifestManager = manifestManager;
        this.applicationProvider = applicationProvider;
    }

    @Override
    public void schedule(ComponentBuilder builder, ComponentDescriptor descriptor) {

        builder.add(new ComponentPartGenerator() {
            public void generate(ComponentDescriptor descriptor) {
                Application applicationAnnotation = descriptor.getTarget().getAnnotation(Application.class);
                ASTType type = descriptor.getTarget();

                org.androidtransfuse.model.manifest.Application manifestApplication = buildManifestEntry(descriptor.getPackageClass().getFullyQualifiedName(), applicationAnnotation);

                manifestApplication.setMetaData(metadataBuilder.buildMetaData(type));

                manifestManager.addApplication(manifestApplication);
            }
        });
    }

    private org.androidtransfuse.model.manifest.Application buildManifestEntry(String name, Application annotation) {

        org.androidtransfuse.model.manifest.Application manifestApplication = applicationProvider.get();

        manifestApplication.setName(name);

        manifestApplication.setLabel(checkBlank(annotation.label()));
        manifestApplication.setAllowTaskReparenting(checkDefault(annotation.allowTaskReparenting(), false));
        manifestApplication.setBackupAgent(checkBlank(annotation.backupAgent()));
        manifestApplication.setDebuggable(checkDefault(annotation.debuggable(), false));
        manifestApplication.setDescription(checkBlank(annotation.description()));
        manifestApplication.setEnabled(checkDefault(annotation.enabled(), true));
        manifestApplication.setHasCode(checkDefault(annotation.hasCode(), true));
        manifestApplication.setHardwareAccelerated(checkDefault(annotation.hardwareAccelerated(), false));
        manifestApplication.setIcon(checkBlank(annotation.icon()));
        manifestApplication.setKillAfterRestore(checkDefault(annotation.killAfterRestore(), true));
        manifestApplication.setLogo(checkBlank(annotation.logo()));
        manifestApplication.setManageSpaceActivity(checkBlank(annotation.manageSpaceActivity()));
        manifestApplication.setPermission(checkBlank(annotation.permission()));
        manifestApplication.setPersistent(checkDefault(annotation.persistent(), false));
        manifestApplication.setProcess(checkBlank(annotation.process()));
        manifestApplication.setRestoreAnyVersion(checkDefault(annotation.restoreAnyVersion(), false));
        manifestApplication.setTaskAffinity(checkBlank(annotation.taskAffinity()));
        manifestApplication.setTheme(checkBlank(annotation.theme()));
        manifestApplication.setUiOptions(checkDefault(annotation.uiOptions(), UIOptions.NONE));
        manifestApplication.setAllowBackup(annotation.allowBackup());
        manifestApplication.setLargeHeap(checkDefault(annotation.largeHeap(), false));
        manifestApplication.setSupportsRtl(checkDefault(annotation.supportsRtl(), false));
        manifestApplication.setRestrictedAccountType(checkDefault(annotation.restrictedAccountType(), false));
        manifestApplication.setVmSafeMode(checkDefault(annotation.vmSafeMode(), false));
        manifestApplication.setTestOnly(checkDefault(annotation.testOnly(), false));
        manifestApplication.setRequiredAccountType(checkBlank(annotation.requiredAccountType()));

        return manifestApplication;
    }

}
