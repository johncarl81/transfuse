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

import org.androidtransfuse.analysis.IntentFilterFactory;
import org.androidtransfuse.analysis.MetaDataBuilder;
import org.androidtransfuse.annotations.BroadcastReceiver;
import org.androidtransfuse.experiment.ComponentBuilder;
import org.androidtransfuse.experiment.ComponentDescriptor;
import org.androidtransfuse.experiment.ComponentPartGenerator;
import org.androidtransfuse.experiment.Generation;
import org.androidtransfuse.model.manifest.Receiver;
import org.androidtransfuse.processor.ManifestManager;

import javax.inject.Inject;
import javax.inject.Provider;

import static org.androidtransfuse.util.AnnotationUtil.checkBlank;
import static org.androidtransfuse.util.AnnotationUtil.checkDefault;

/**
 * @author John Ericksen
 */
public class BroadcastReceiverManifestEntryGenerator implements Generation {

    private final IntentFilterFactory intentFilterBuilder;
    private final MetaDataBuilder metadataBuilder;
    private final ManifestManager manifestManager;
    private final Provider<Receiver> receiverProvider;

    @Inject
    public BroadcastReceiverManifestEntryGenerator(IntentFilterFactory intentFilterBuilder,
                                                   MetaDataBuilder metadataBuilder,
                                                   ManifestManager manifestManager,
                                                   Provider<Receiver> receiverProvider) {
        this.intentFilterBuilder = intentFilterBuilder;
        this.metadataBuilder = metadataBuilder;
        this.manifestManager = manifestManager;
        this.receiverProvider = receiverProvider;
    }

    @Override
    public String getName() {
        return "Manifest BroadcastReceiver Generator";
    }

    @Override
    public void schedule(ComponentBuilder builder, ComponentDescriptor descriptor) {
        if(descriptor.getTarget() != null && descriptor.getTarget().isAnnotated(BroadcastReceiver.class)) {
            builder.add(new ComponentPartGenerator() {
                @Override
                public void generate(ComponentDescriptor descriptor) {
                    BroadcastReceiver annotation = descriptor.getTarget().getAnnotation(BroadcastReceiver.class);

                    Receiver manifestReceiver = buildReceiver(descriptor.getPackageClass().getFullyQualifiedName(), annotation);

                    manifestReceiver.setIntentFilters(intentFilterBuilder.buildIntentFilters(descriptor.getTarget()));
                    manifestReceiver.setMetaData(metadataBuilder.buildMetaData(descriptor.getTarget()));

                    manifestManager.addBroadcastReceiver(manifestReceiver);
                }
            });
        }
    }

    protected Receiver buildReceiver(String name, BroadcastReceiver annotation) {
        Receiver manifestReceiver = receiverProvider.get();

        manifestReceiver.setName(name);
        manifestReceiver.setLabel(checkBlank(annotation.label()));
        manifestReceiver.setProcess(checkBlank(annotation.process()));
        manifestReceiver.setPermission(checkBlank(annotation.permission()));
        manifestReceiver.setIcon(checkBlank(annotation.icon()));
        manifestReceiver.setEnabled(checkDefault(annotation.enabled(), true));
        manifestReceiver.setExported(annotation.exported().getValue());

        return manifestReceiver;
    }
}
