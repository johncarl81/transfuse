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

import javax.inject.Inject;
import java.util.Arrays;

/**
 * @author John Ericksen
 */
public class InstallProcessor implements TypeProcessor {

    private final ModuleRepository moduleRepository;

    @Inject
    public InstallProcessor(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    @Override
    public ModuleConfiguration process(ASTType moduleType, ASTAnnotation installAnnotation) {
        ASTType[] types = installAnnotation.getProperty("value", ASTType[].class);

        return new InstallModuleConfiguration(types);
    }

    private final class InstallModuleConfiguration implements ModuleConfiguration{

        private final ASTType[] types;

        private InstallModuleConfiguration(ASTType[] types) {
            this.types = Arrays.copyOf(types, types.length);
        }

        @Override
        public void setConfiguration(InjectionNodeBuilderRepository configurationRepository) {
            if(types != null && types.length > 0){
                moduleRepository.addInstalledComponents(types);
            }
        }
    }


}
