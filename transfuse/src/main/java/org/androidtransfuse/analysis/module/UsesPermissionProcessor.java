package org.androidtransfuse.analysis.module;

import org.androidtransfuse.adapter.ASTAnnotation;
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
    public ModuleConfiguration process(ASTAnnotation bindAnnotation) {
        String[] usesPermissions = bindAnnotation.getProperty("value", String[].class);

        return new UsesPermissionModuleConfiguration(usesPermissions);
    }

    private final class UsesPermissionModuleConfiguration implements ModuleConfiguration{

        private final String[] usesPermissions;

        private UsesPermissionModuleConfiguration(String[] usesPermissions) {
            this.usesPermissions = usesPermissions;
        }


        @Override
        public void setConfiguration() {
            for (String permission : usesPermissions) {
                manifestManager.addUsesPermission(permission);
            }
        }
    }
}
