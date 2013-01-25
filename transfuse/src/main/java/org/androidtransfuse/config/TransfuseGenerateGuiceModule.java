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
package org.androidtransfuse.config;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import org.androidtransfuse.analysis.AnalysisContextFactory;
import org.androidtransfuse.analysis.repository.AnalysisRepository;
import org.androidtransfuse.analysis.repository.AnalysisRepositoryFactory;
import org.androidtransfuse.analysis.repository.ScopeAspectFactoryRepository;
import org.androidtransfuse.analysis.repository.ScopeAspectFactoryRepositoryProvider;
import org.androidtransfuse.gen.GeneratorFactory;
import org.androidtransfuse.gen.InjectionBuilderContextFactory;
import org.androidtransfuse.gen.componentBuilder.ComponentBuilderFactory;
import org.androidtransfuse.gen.variableBuilder.InjectionNodeBuilder;
import org.androidtransfuse.gen.variableBuilder.VariableInjectionBuilderFactory;
import org.androidtransfuse.gen.variableBuilder.VariableInjectionNodeBuilder;
import org.androidtransfuse.gen.variableBuilder.resource.MethodBasedResourceExpressionBuilderAdaptorFactory;
import org.androidtransfuse.gen.variableBuilder.resource.MethodBasedResourceExpressionBuilderFactory;
import org.androidtransfuse.gen.variableDecorator.ExpressionDecoratorFactory;
import org.androidtransfuse.gen.variableDecorator.VariableExpressionBuilder;
import org.androidtransfuse.gen.variableDecorator.VariableExpressionBuilderFactory;
import org.androidtransfuse.model.manifest.Manifest;
import org.androidtransfuse.model.r.RResource;
import org.androidtransfuse.processor.GeneratorRepository;
import org.androidtransfuse.processor.GeneratorRepositoryProvider;

import java.io.File;

/**
 * @author John Ericksen
 */
public class TransfuseGenerateGuiceModule extends AbstractModule {

    public static final String CONFIGURATION_SCOPE = "configurationScope";
    public static final String ORIGINAL_MANIFEST = "originalManifest";
    public static final String DEFAULT_BINDING = "defaultBinding";
    public static final String MANIFEST_FILE = "manifestFile";

    private final EnterableScope configurationScope;

    public TransfuseGenerateGuiceModule(EnterableScope configurationScope) {
        this.configurationScope = configurationScope;
    }

    @Override
    protected void configure() {

        FactoryModuleBuilder factoryModuleBuilder = new FactoryModuleBuilder();

        install(factoryModuleBuilder.build(VariableInjectionBuilderFactory.class));
        install(factoryModuleBuilder.build(MethodBasedResourceExpressionBuilderFactory.class));
        install(factoryModuleBuilder.build(MethodBasedResourceExpressionBuilderAdaptorFactory.class));
        install(factoryModuleBuilder.build(VariableExpressionBuilderFactory.class));
        install(factoryModuleBuilder.build(ComponentBuilderFactory.class));
        install(factoryModuleBuilder.build(AnalysisContextFactory.class));
        install(factoryModuleBuilder.build(InjectionBuilderContextFactory.class));
        install(factoryModuleBuilder.build(GeneratorFactory.class));

        bind(InjectionNodeBuilder.class).annotatedWith(Names.named(DEFAULT_BINDING)).to(VariableInjectionNodeBuilder.class);

        bind(VariableExpressionBuilder.class).toProvider(ExpressionDecoratorFactory.class);
        bind(ScopeAspectFactoryRepository.class).toProvider(ScopeAspectFactoryRepositoryProvider.class);
        bind(AnalysisRepository.class).toProvider(AnalysisRepositoryFactory.class).in(ConfigurationScope.class);

        bind(GeneratorRepository.class).toProvider(GeneratorRepositoryProvider.class);

        bindScope(ConfigurationScope.class, configurationScope);
        bind(EnterableScope.class).annotatedWith(Names.named(CONFIGURATION_SCOPE)).toInstance(configurationScope);

        bind(Key.get(Manifest.class, Names.named(ORIGINAL_MANIFEST))).toProvider(new ThrowingProvider<Manifest>()).in(ConfigurationScope.class);
        bind(RResource.class).toProvider(new ThrowingProvider<RResource>()).in(ConfigurationScope.class);
        bind(Key.get(File.class, Names.named(MANIFEST_FILE))).toProvider(new ThrowingProvider<File>()).in(ConfigurationScope.class);
    }
}
