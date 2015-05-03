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
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.experiment.ComponentBuilder;
import org.androidtransfuse.experiment.ComponentDescriptor;
import org.androidtransfuse.experiment.ComponentPartGenerator;
import org.androidtransfuse.experiment.Generation;
import org.androidtransfuse.processor.ManifestManager;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.inject.Provider;

import static org.androidtransfuse.util.AnnotationUtil.checkBlank;
import static org.androidtransfuse.util.AnnotationUtil.checkDefault;

/**
 * @author John Ericksen
 */
public class ActivityManifestEntryGenerator implements Generation {

    private final IntentFilterFactory intentFilterBuilder;
    private final MetaDataBuilder metadataBuilder;
    private final ManifestManager manifestManager;
    private final Provider<org.androidtransfuse.model.manifest.Activity> manifestActivityProvider;

    @Inject
    public ActivityManifestEntryGenerator(IntentFilterFactory intentFilterBuilder,
                                          MetaDataBuilder metadataBuilder,
                                          ManifestManager manifestManager,
                                          Provider<org.androidtransfuse.model.manifest.Activity> manifestActivityProvider) {
        this.intentFilterBuilder = intentFilterBuilder;
        this.metadataBuilder = metadataBuilder;
        this.manifestManager = manifestManager;
        this.manifestActivityProvider = manifestActivityProvider;
    }

    @Override
    public String getName() {
        return "Manifest Activity Generator";
    }

    @Override
    public void schedule(ComponentBuilder builder, ComponentDescriptor descriptor) {

        if(descriptor.getTarget() != null && descriptor.getTarget().isAnnotated(Activity.class)) {
            builder.add(new ComponentPartGenerator() {
                public void generate(ComponentDescriptor descriptor) {
                    Activity activityAnnotation = descriptor.getTarget().getAnnotation(Activity.class);
                    ASTType type = descriptor.getTarget();

                    org.androidtransfuse.model.manifest.Activity manifestActivity = buildManifestEntry(descriptor.getPackageClass().getFullyQualifiedName(), activityAnnotation);

                    manifestActivity.setIntentFilters(intentFilterBuilder.buildIntentFilters(type));
                    manifestActivity.setMetaData(metadataBuilder.buildMetaData(type));

                    manifestManager.addActivity(manifestActivity);
                }
            });
        }
    }

    protected org.androidtransfuse.model.manifest.Activity buildManifestEntry(String name, Activity activityAnnotation) {
        org.androidtransfuse.model.manifest.Activity manifestActivity = manifestActivityProvider.get();

        manifestActivity.setName(name);
        manifestActivity.setLabel(checkBlank(activityAnnotation.label()));
        manifestActivity.setAllowTaskReparenting(checkDefault(activityAnnotation.allowTaskReparenting(), false));
        manifestActivity.setAlwaysRetainTaskState(checkDefault(activityAnnotation.alwaysRetainTaskState(), false));
        manifestActivity.setClearTaskOnLaunch(checkDefault(activityAnnotation.clearTaskOnLaunch(), false));
        manifestActivity.setConfigChanges(concatenate(activityAnnotation.configChanges()));
        manifestActivity.setEnabled(checkDefault(activityAnnotation.enabled(), true));
        manifestActivity.setExcludeFromRecents(checkDefault(activityAnnotation.excludeFromRecents(), false));
        manifestActivity.setExported(activityAnnotation.exported().getValue());
        manifestActivity.setFinishOnTaskLaunch(checkDefault(activityAnnotation.finishOnTaskLaunch(), false));
        manifestActivity.setHardwareAccelerated(checkDefault(activityAnnotation.hardwareAccelerated(), false));
        manifestActivity.setIcon(checkBlank(activityAnnotation.icon()));
        manifestActivity.setLaunchMode(checkDefault(activityAnnotation.launchMode(), LaunchMode.STANDARD));
        manifestActivity.setMultiprocess(checkDefault(activityAnnotation.multiprocess(), false));
        manifestActivity.setNoHistory(checkDefault(activityAnnotation.noHistory(), false));
        manifestActivity.setPermission(checkBlank(activityAnnotation.permission()));
        manifestActivity.setProcess(checkBlank(activityAnnotation.process()));
        manifestActivity.setScreenOrientation(checkDefault(activityAnnotation.screenOrientation(), ScreenOrientation.UNSPECIFIED));
        manifestActivity.setStateNotNeeded(checkDefault(activityAnnotation.stateNotNeeded(), false));
        manifestActivity.setTaskAffinity(checkBlank(activityAnnotation.taskAffinity()));
        manifestActivity.setTheme(checkBlank(activityAnnotation.theme()));
        manifestActivity.setUiOptions(checkDefault(activityAnnotation.uiOptions(), UIOptions.NONE));
        manifestActivity.setWindowSoftInputMode(checkDefault(activityAnnotation.windowSoftInputMode(), WindowSoftInputMode.STATE_UNSPECIFIED));

        return manifestActivity;
    }

    private String concatenate(ConfigChanges[] configChanges) {
        if (configChanges.length == 0) {
            return null;
        }

        return StringUtils.join(configChanges, "|");
    }
}
