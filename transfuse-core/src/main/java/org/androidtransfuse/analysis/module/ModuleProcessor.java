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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.annotations.*;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Central module processor class.  Scans the input AST elements for the appropriate annotations and registers
 * the results with the given processor.
 *
 * @author John Ericksen
 */
public class ModuleProcessor {

    private final ImmutableMap<ASTType, MethodProcessor> methodProcessors;
    private final ImmutableMap<ASTType, TypeProcessor> typeProcessors;
    private final ModuleRepository moduleRepository;
    private final Provider<InjectionNodeBuilderRepository> injectionNodeBuilderRepositoryProvider;

    @Inject
    public ModuleProcessor(BindProcessor bindProcessor,
                           BindProviderProcessor bindProviderProcessor,
                           BindingConfigurationFactory configurationFactory,
                           ProvidesProcessor providesProcessor,
                           ASTClassFactory astClassFactory,
                           DefineScopeProcessor defineScopeProcessor,
                           InstallProcessor installComponentProcessor,
                           ModuleRepository moduleRepository,
                           Provider<InjectionNodeBuilderRepository> injectionNodeBuilderRepositoryProvider,
                           NamespaceProcessor namespaceProcessor) {
        this.moduleRepository = moduleRepository;
        this.injectionNodeBuilderRepositoryProvider = injectionNodeBuilderRepositoryProvider;
        ImmutableMap.Builder<ASTType, MethodProcessor> methodProcessorsBuilder = ImmutableMap.builder();

        methodProcessorsBuilder.put(astClassFactory.getType(Provides.class), providesProcessor);

        this.methodProcessors = methodProcessorsBuilder.build();

        ImmutableMap.Builder<ASTType, TypeProcessor> typeProcessorsBuilder = ImmutableMap.builder();
        typeProcessorsBuilder.put(astClassFactory.getType(BindProvider.class), bindProviderProcessor);
        typeProcessorsBuilder.put(astClassFactory.getType(BindProviders.class),
                configurationFactory.buildConfigurationComposite(bindProviderProcessor));
        typeProcessorsBuilder.put(astClassFactory.getType(Bind.class), bindProcessor);
        typeProcessorsBuilder.put(astClassFactory.getType(Bindings.class),
                configurationFactory.buildConfigurationComposite(bindProcessor));
        typeProcessorsBuilder.put(astClassFactory.getType(DefineScope.class), defineScopeProcessor);
        typeProcessorsBuilder.put(astClassFactory.getType(DefineScopes.class),
                configurationFactory.buildConfigurationComposite(defineScopeProcessor));
        typeProcessorsBuilder.put(astClassFactory.getType(Install.class), installComponentProcessor);
        typeProcessorsBuilder.put(astClassFactory.getType(Namespace.class), namespaceProcessor);


        typeProcessors = typeProcessorsBuilder.build();
    }

    public Void process(ASTType astType) {

        ImmutableList.Builder<ModuleConfiguration> configurations = ImmutableList.builder();

        for (ASTAnnotation typeAnnotation : astType.getAnnotations()) {
            if(typeProcessors.containsKey(typeAnnotation.getASTType())){
                TypeProcessor typeProcessor = typeProcessors.get(typeAnnotation.getASTType());

                configurations.add(typeProcessor.process(astType, typeAnnotation));
            }
        }

        for (ASTMethod astMethod : astType.getMethods()) {
            for (ASTAnnotation astAnnotation : astMethod.getAnnotations()) {
                if (methodProcessors.containsKey(astAnnotation.getASTType())) {
                    MethodProcessor methodProcessor = methodProcessors.get(astAnnotation.getASTType());

                    configurations.add(methodProcessor.process(astType, astMethod, astAnnotation));
                }
            }
        }

        InjectionNodeBuilderRepository repository = injectionNodeBuilderRepositoryProvider.get();
        for (ModuleConfiguration moduleConfiguration : configurations.build()) {
            moduleConfiguration.setConfiguration(repository);
        }
        moduleRepository.addModuleRepository(repository);

        return null;
    }
}
