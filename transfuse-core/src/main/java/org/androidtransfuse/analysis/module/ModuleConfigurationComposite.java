/**
 * Copyright 2013 John Ericksen
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

import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Combines multiple configurations into a single configuration point.
 *
 * @author John Ericksen
 */
public class ModuleConfigurationComposite implements ModuleConfiguration {

    private List<ModuleConfiguration> moduleConfigurations = new ArrayList<ModuleConfiguration>();

    @Override
    public void setConfiguration(InjectionNodeBuilderRepository configurationRepository) {
        for (ModuleConfiguration moduleConfiguration : moduleConfigurations) {
            moduleConfiguration.setConfiguration(configurationRepository);
        }
    }

    public void add(ModuleConfiguration configuration){
        this.moduleConfigurations.add(configuration);
    }
}
