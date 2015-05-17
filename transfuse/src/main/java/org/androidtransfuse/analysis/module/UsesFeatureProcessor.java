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
package org.androidtransfuse.analysis.module;

import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.model.manifest.UsesFeature;
import org.androidtransfuse.processor.ManifestManager;

import javax.inject.Inject;

import static org.androidtransfuse.util.AnnotationUtil.checkBlank;
import static org.androidtransfuse.util.AnnotationUtil.checkDefault;

/**
 * @author Gustavo Matias
 */
public class UsesFeatureProcessor implements TypeProcessor {

    private final ManifestManager manifestManager;

    @Inject
    public UsesFeatureProcessor(ManifestManager manifestManager) {
        this.manifestManager = manifestManager;
    }

    @Override
    public ModuleConfiguration process(ASTType rootModuleType, ASTType moduleAncestor, ASTAnnotation permissionAnnotation) {
    	UsesFeature usesFeature = new UsesFeature();
        usesFeature.setName(checkBlank(permissionAnnotation.getProperty("value", String.class)));
        usesFeature.setRequired(checkDefault(permissionAnnotation.getProperty("required", Boolean.class), true));
        usesFeature.setGlEsVersion(checkDefault(permissionAnnotation.getProperty("glEsVersion", Integer.class), -1));

        return new UsesFeatureModuleConfiguration(usesFeature);
    }

    private final class UsesFeatureModuleConfiguration implements ModuleConfiguration{

        private final UsesFeature usesFeature;

        private UsesFeatureModuleConfiguration(UsesFeature usesFeature) {
            this.usesFeature = usesFeature;
        }


        @Override
        public void setConfiguration(InjectionNodeBuilderRepository configurationRepository) {
            manifestManager.addUsesFeature(usesFeature);
        }
    }
}
