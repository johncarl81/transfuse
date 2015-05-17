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
import org.androidtransfuse.annotations.ProtectionLevel;
import org.androidtransfuse.model.manifest.Permission;
import org.androidtransfuse.processor.ManifestManager;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class PermissionProcessor implements TypeProcessor {

    private final ManifestManager manifestManager;

    @Inject
    public PermissionProcessor(ManifestManager manifestManager) {
        this.manifestManager = manifestManager;
    }

    @Override
    public ModuleConfiguration process(ASTType rootModuleType, ASTType moduleAncestor, ASTAnnotation permissionAnnotation) {
        Permission permission = new Permission();
        permission.setName(permissionAnnotation.getProperty("name", String.class));
        permission.setDescription(permissionAnnotation.getProperty("description", String.class));
        permission.setIcon(permissionAnnotation.getProperty("icon", String.class));
        permission.setLabel(permissionAnnotation.getProperty("label", String.class));
        permission.setPermissionGroup(permissionAnnotation.getProperty("permissionGroup", String.class));
        permission.setProtectionLevel(permissionAnnotation.getProperty("protectionLevel", ProtectionLevel.class));

        return new PermissionModuleConfiguration(permission);
    }

    private final class PermissionModuleConfiguration implements ModuleConfiguration{

        private final Permission permission;

        private PermissionModuleConfiguration(Permission permission) {
            this.permission = permission;
        }


        @Override
        public void setConfiguration(InjectionNodeBuilderRepository configurationRepository) {
            manifestManager.addPermission(permission);
        }
    }
}
