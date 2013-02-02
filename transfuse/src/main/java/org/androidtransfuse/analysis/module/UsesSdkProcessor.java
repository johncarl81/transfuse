package org.androidtransfuse.analysis.module;

import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.model.manifest.UsesSDK;
import org.androidtransfuse.processor.ManifestManager;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class UsesSdkProcessor implements TypeProcessor {

    private final ManifestManager manifestManager;

    @Inject
    public UsesSdkProcessor(ManifestManager manifestManager) {
        this.manifestManager = manifestManager;
    }

    @Override
    public ModuleConfiguration process(ASTAnnotation bindAnnotation) {
        Integer min = bindAnnotation.getProperty("min", Integer.class);
        Integer target = bindAnnotation.getProperty("target", Integer.class);
        Integer max = bindAnnotation.getProperty("max", Integer.class);

        return new UsesSdkModuleConfiguration(min, target, max);
    }

    private final class UsesSdkModuleConfiguration implements ModuleConfiguration{

        private final Integer min;
        private final Integer target;
        private final Integer max;

        private UsesSdkModuleConfiguration(Integer min, Integer target, Integer max) {
            this.min = min;
            this.target = target;
            this.max = max;
        }

        @Override
        public void setConfiguration() {
            UsesSDK usesSDK = new UsesSDK();
            boolean valueExists = false;
            if(min != null && min >= 0){
                usesSDK.setMinSdkVersion(min);
                valueExists = true;
            }
            if(target != null && target >= 0){
                usesSDK.setTargetSdkVersion(target);
                valueExists = true;
            }
            if(max != null && max >= 0){
                usesSDK.setMaxSdkVersion(max);
                valueExists = true;
            }
            if(valueExists){
                manifestManager.setUsesSdk(usesSDK);
            }
        }
    }
}
