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

import org.androidtransfuse.ConfigurationRepository;
import org.androidtransfuse.TransfuseAnalysisException;
import org.androidtransfuse.TransfusePlugin;
import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.bootstrap.Bootstrap;
import org.androidtransfuse.bootstrap.Bootstraps;
import org.androidtransfuse.scope.Scopes;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class PluginProcessor implements TypeProcessor {

    private final ConfigurationRepository repository;
    private final Scopes scopes;

    @Inject
    public PluginProcessor(ConfigurationRepository repository, Scopes scopes) {
        this.repository = repository;
        this.scopes = scopes;
    }

    @Override
    public ModuleConfiguration process(ASTType moduleType, ASTAnnotation typeAnnotation) {

        final ASTType pluginType = typeAnnotation.getProperty("value", ASTType.class);

        return new ModuleConfiguration() {
            @Override
            public void setConfiguration(InjectionNodeBuilderRepository configurationRepository) {
                try {
                    Class<TransfusePlugin> pluginClass = (Class<TransfusePlugin>) Class.forName(pluginType.getName());
                    TransfusePlugin plugin = pluginClass.newInstance();
                    if(pluginClass.isAnnotationPresent(Bootstrap.class)) {
                        Bootstraps.getInjector(pluginClass).inject(scopes, plugin);
                    }
                    plugin.run(repository);
                } catch (InstantiationException e) {
                    throw new TransfuseAnalysisException("Unable to instantiate", e);
                } catch (IllegalAccessException e) {
                    throw new TransfuseAnalysisException("IllegalAccessException", e);
                } catch (ClassNotFoundException e) {
                    throw new TransfuseAnalysisException("ClassNotFoundException", e);
                }
            }
        };
    }
}
