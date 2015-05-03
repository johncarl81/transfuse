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
package org.androidtransfuse.experiment.generators;

import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.analysis.IntentFilterFactory;
import org.androidtransfuse.analysis.MetaDataBuilder;
import org.androidtransfuse.annotations.Service;
import org.androidtransfuse.experiment.ComponentBuilder;
import org.androidtransfuse.experiment.ComponentDescriptor;
import org.androidtransfuse.experiment.ComponentPartGenerator;
import org.androidtransfuse.experiment.Generation;
import org.androidtransfuse.processor.ManifestManager;

import javax.inject.Inject;
import javax.inject.Provider;

import static org.androidtransfuse.util.AnnotationUtil.checkBlank;
import static org.androidtransfuse.util.AnnotationUtil.checkDefault;

/**
 * @author John Ericksen
 */
public class ServiceManifestEntryGenerator implements Generation {

    private final IntentFilterFactory intentFilterBuilder;
    private final MetaDataBuilder metadataBuilder;
    private final ManifestManager manifestManager;
    private final Provider<org.androidtransfuse.model.manifest.Service> manifestServiceProvider;

    @Inject
    public ServiceManifestEntryGenerator(IntentFilterFactory intentFilterBuilder,
                                         MetaDataBuilder metadataBuilder,
                                         ManifestManager manifestManager,
                                         Provider<org.androidtransfuse.model.manifest.Service> manifestServiceProvider) {
        this.intentFilterBuilder = intentFilterBuilder;
        this.metadataBuilder = metadataBuilder;
        this.manifestManager = manifestManager;
        this.manifestServiceProvider = manifestServiceProvider;
    }

    @Override
    public String getName() {
        return "Manifest Service Generator";
    }

    @Override
    public void schedule(ComponentBuilder builder, ComponentDescriptor descriptor) {
        if(descriptor.getTarget() != null && descriptor.getTarget().isAnnotated(Service.class)) {
            builder.add(new ComponentPartGenerator() {
                public void generate(ComponentDescriptor descriptor) {
                    Service serviceAnnotation = descriptor.getTarget().getAnnotation(Service.class);
                    ASTType type = descriptor.getTarget();

                    org.androidtransfuse.model.manifest.Service manifestService = buildService(descriptor.getPackageClass().getFullyQualifiedName(), serviceAnnotation);

                    manifestService.setIntentFilters(intentFilterBuilder.buildIntentFilters(type));
                    manifestService.setMetaData(metadataBuilder.buildMetaData(type));

                    manifestManager.addService(manifestService);
                }
            });
        }
    }

    protected org.androidtransfuse.model.manifest.Service buildService(String name, Service serviceAnnotation) {
        org.androidtransfuse.model.manifest.Service manifestService = manifestServiceProvider.get();

        manifestService.setName(name);
        manifestService.setEnabled(checkDefault(serviceAnnotation.enabled(), true));
        manifestService.setExported(serviceAnnotation.exported().getValue());
        manifestService.setIcon(checkBlank(serviceAnnotation.icon()));
        manifestService.setLabel(checkBlank(serviceAnnotation.label()));
        manifestService.setPermission(checkBlank(serviceAnnotation.permission()));
        manifestService.setProcess(checkBlank(serviceAnnotation.process()));

        return manifestService;
    }
}
