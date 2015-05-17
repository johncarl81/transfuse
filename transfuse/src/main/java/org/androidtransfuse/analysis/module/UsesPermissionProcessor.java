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
import org.androidtransfuse.model.manifest.UsesPermission;
import org.androidtransfuse.processor.ManifestManager;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class UsesPermissionProcessor implements TypeProcessor {

    private final ManifestManager manifestManager;

    @Inject
    public UsesPermissionProcessor(ManifestManager manifestManager) {
        this.manifestManager = manifestManager;
    }

    @Override
    public ModuleConfiguration process(ASTType rootModuleType, ASTType moduleAncestor, ASTAnnotation bindAnnotation) {
        UsesPermission permission = new UsesPermission();
        permission.setName(bindAnnotation.getProperty("name", String.class));
        Integer maxSdkVersion = bindAnnotation.getProperty("maxSdkVersion", Integer.class);
        if(maxSdkVersion != null && maxSdkVersion > 0){
            permission.setMaxSdkVersion(maxSdkVersion);
        }

        return new UsesPermissionModuleConfiguration(permission);
    }

    private final class UsesPermissionModuleConfiguration implements ModuleConfiguration{

        private final UsesPermission usesPermission;

        private UsesPermissionModuleConfiguration(UsesPermission usesPermission) {
            this.usesPermission = usesPermission;
        }


        @Override
        public void setConfiguration(InjectionNodeBuilderRepository configurationRepository) {
            manifestManager.addUsesPermission(usesPermission);
        }
    }
}
