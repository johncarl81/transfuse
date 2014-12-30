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
    public ModuleConfiguration process(ASTType moduleType, ASTAnnotation bindAnnotation) {
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
        public void setConfiguration(InjectionNodeBuilderRepository configurationRepository) {
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
