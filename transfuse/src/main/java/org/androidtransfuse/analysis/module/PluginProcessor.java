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

import org.androidtransfuse.TransfusePlugin;
import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class PluginProcessor implements TypeProcessor {

    private final ModuleRepository moduleRepository;

    @Inject
    public PluginProcessor(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    @Override
    public ModuleConfiguration process(ASTType moduleType, ASTAnnotation typeAnnotation) {

        ASTType pluginType = typeAnnotation.getProperty("value", ASTType.class);

        try {

            TransfusePlugin plugin = (TransfusePlugin) Class.forName(pluginType.getName()).newInstance();
            plugin.run();

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return new ModuleConfiguration() {
            @Override
            public void setConfiguration(InjectionNodeBuilderRepository configurationRepository) {
                //empty
            }
        };
    }
}
