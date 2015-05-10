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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import org.androidtransfuse.Plugin;
import org.androidtransfuse.Plugins;
import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.transaction.AbstractCompletionTransactionWorker;

import javax.inject.Inject;
import javax.inject.Provider;
import java.lang.annotation.Annotation;

/**
 * Central module processor class.  Scans the input AST elements for the appropriate annotations and registers
 * the results with the given processor.
 *
 * @author John Ericksen
 */
public class ModuleTransactionWorker extends AbstractCompletionTransactionWorker<Provider<ASTType>, Void> {

    private final ImmutableMap<ASTType, MethodProcessor> methodProcessors;
    private final ImmutableMap<ASTType, TypeProcessor> typeProcessors;
    private final ModuleRepository moduleRepository;
    private final Provider<InjectionNodeBuilderRepository> injectionNodeBuilderRepositoryProvider;

    @Inject
    public ModuleTransactionWorker(BindProcessor bindProcessor,
                                   BindProviderProcessor bindProviderProcessor,
                                   BindInterceptorProcessor bindInterceptorProcessor,
                                   BindingConfigurationFactory configurationFactory,
                                   ProvidesProcessor providesProcessor,
                                   ASTClassFactory astClassFactory,
                                   UsesPermissionProcessor usesPermissionProcessor,
                                   UsesSdkProcessor usesSdkProcessor,
                                   DefineScopeProcessor defineScopeProcessor,
                                   PermissionProcessor permissionProcessor,
                                   UsesFeatureProcessor usesFeatureProcessor,
                                   ModuleRepository moduleRepository,
                                   PluginProcessor pluginProcessor,
                                   Provider<InjectionNodeBuilderRepository> injectionNodeBuilderRepositoryProvider) {
        this.moduleRepository = moduleRepository;
        this.injectionNodeBuilderRepositoryProvider = injectionNodeBuilderRepositoryProvider;
        ImmutableMap.Builder<ASTType, MethodProcessor> methodProcessorsBuilder = ImmutableMap.builder();

        methodProcessorsBuilder.put(astClassFactory.getType(Provides.class), providesProcessor);

        this.methodProcessors = methodProcessorsBuilder.build();
        ImmutableMap.Builder<ASTType, TypeProcessor> typeProcessorsBuilder = ImmutableMap.builder();

        setupProcessor(typeProcessorsBuilder, astClassFactory, configurationFactory, BindInterceptor.class, BindInterceptors.class, bindInterceptorProcessor);
        setupProcessor(typeProcessorsBuilder, astClassFactory, configurationFactory, BindProvider.class, BindProviders.class, bindProviderProcessor);
        setupProcessor(typeProcessorsBuilder, astClassFactory, configurationFactory, Bind.class, Bindings.class, bindProcessor);
        setupProcessor(typeProcessorsBuilder, astClassFactory, configurationFactory, UsesPermission.class, UsesPermissions.class, usesPermissionProcessor);
        setupProcessor(typeProcessorsBuilder, astClassFactory, configurationFactory, Permission.class, Permissions.class, permissionProcessor);
        setupProcessor(typeProcessorsBuilder, astClassFactory, configurationFactory, UsesFeature.class, UsesFeatures.class, usesFeatureProcessor);
        setupProcessor(typeProcessorsBuilder, astClassFactory, configurationFactory, DefineScope.class, DefineScopes.class, defineScopeProcessor);
        setupProcessor(typeProcessorsBuilder, astClassFactory, configurationFactory, Plugin.class, Plugins.class, pluginProcessor);
        typeProcessorsBuilder.put(astClassFactory.getType(UsesSdk.class), usesSdkProcessor);

        typeProcessors = typeProcessorsBuilder.build();
    }

    private void setupProcessor(ImmutableMap.Builder<ASTType, TypeProcessor> typeProcessorsBuilder,
                                ASTClassFactory astClassFactory,
                                BindingConfigurationFactory configurationFactory,
                                Class<? extends Annotation> processorAnnotation,
                                Class<? extends Annotation> pluralAnnotation,
                                TypeProcessor processor) {
        typeProcessorsBuilder.put(astClassFactory.getType(processorAnnotation), processor);
        typeProcessorsBuilder.put(astClassFactory.getType(pluralAnnotation),
                configurationFactory.buildConfigurationComposite(processor));
    }

    @Override
    public Void innerRun(Provider<ASTType> astTypeProvider) {
        ImmutableList<ModuleConfiguration> configurations = createConfigurationsForModuleType(astTypeProvider.get());

        InjectionNodeBuilderRepository repository = injectionNodeBuilderRepositoryProvider.get();
        for (ModuleConfiguration moduleConfiguration : configurations) {
            moduleConfiguration.setConfiguration(repository);
        }
        moduleRepository.addModuleRepository(repository);

        return null;
    }

    private ImmutableList<ModuleConfiguration> createConfigurationsForModuleType(ASTType moduleType) {
        ImmutableList.Builder<ModuleConfiguration> configurations = ImmutableList.builder();
        createConfigurationsForModuleType(configurations, moduleType, moduleType.getSuperClass());
        return configurations.build();
    }

    /**
     * Recursive method that finds module methods and annotations on all parents of moduleAncestor and moduleAncestor, but creates configuration based on the module.
     * This allows any class in the module hierarchy to contribute annotations or providers that can be overridden by subclasses.
     *
     * @param configurations the holder for annotation and method configurations
     * @param module the module type we are configuring
     * @param moduleAncestor either the model type or one of its ancestor types on which to look for module methods and annotations
     */
    private void createConfigurationsForModuleType(ImmutableList.Builder<ModuleConfiguration> configurations, ASTType module, ASTType moduleAncestor) {
        // if super type is null, we are at the top of the inheritance hierarchy and we can unwind.
        ASTType target = module;

        if(moduleAncestor != null) {
            // recurse our way up the tree so we add the top most providers first and clobber them on the way down
            createConfigurationsForModuleType(configurations, module, moduleAncestor.getSuperClass());
            target = moduleAncestor;
        }
        configureModuleAnnotations(configurations, module, target.getAnnotations());
        configureModuleMethods(configurations, module, target.getMethods());
    }

    private void configureModuleMethods(ImmutableList.Builder<ModuleConfiguration> configurations, ASTType moduleType, ImmutableSet<ASTMethod> methods) {
        for (ASTMethod astMethod : methods) {
            for (ASTAnnotation astAnnotation : astMethod.getAnnotations()) {
                if (methodProcessors.containsKey(astAnnotation.getASTType())) {
                    MethodProcessor methodProcessor = methodProcessors.get(astAnnotation.getASTType());
                    configurations.add(methodProcessor.process(moduleType, astMethod, astAnnotation));
                }
            }
        }
    }

    private void configureModuleAnnotations(ImmutableList.Builder<ModuleConfiguration> configurations, ASTType moduleType, ImmutableSet<ASTAnnotation> annotations) {
        for (ASTAnnotation typeAnnotation : annotations) {
            if (typeProcessors.containsKey(typeAnnotation.getASTType())) {
                TypeProcessor typeProcessor = typeProcessors.get(typeAnnotation.getASTType());
                configurations.add(typeProcessor.process(moduleType, typeAnnotation));
            }
        }
    }
}
