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

import com.sun.codemodel.JCodeModel;
import org.androidtransfuse.*;
import org.androidtransfuse.adapter.ASTFactory;
import org.androidtransfuse.analysis.module.ModuleRepository;
import org.androidtransfuse.analysis.repository.AnalysisRepository;
import org.androidtransfuse.analysis.repository.AnalysisRepositoryFactory;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepositoryFactory;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.aop.AsynchronousMethodInterceptor;
import org.androidtransfuse.aop.UIThreadMethodInterceptor;
import org.androidtransfuse.bootstrap.BootstrapModule;
import org.androidtransfuse.gen.*;
import org.androidtransfuse.gen.componentBuilder.ComponentBuilderFactory;
import org.androidtransfuse.gen.invocationBuilder.DefaultInvocationBuilderStrategy;
import org.androidtransfuse.gen.invocationBuilder.InvocationBuilderStrategy;
import org.androidtransfuse.gen.variableBuilder.VariableInjectionBuilderFactory;
import org.androidtransfuse.gen.variableBuilder.resource.MethodBasedResourceExpressionBuilderAdaptorFactory;
import org.androidtransfuse.gen.variableBuilder.resource.MethodBasedResourceExpressionBuilderFactory;
import org.androidtransfuse.gen.variableDecorator.ExpressionDecoratorFactory;
import org.androidtransfuse.gen.variableDecorator.VariableExpressionBuilder;
import org.androidtransfuse.gen.variableDecorator.VariableExpressionBuilderFactory;
import org.androidtransfuse.model.manifest.Manifest;
import org.androidtransfuse.model.r.RResource;
import org.androidtransfuse.processor.AnalysisGenerationTransactionProcessorBuilderFactory;
import org.androidtransfuse.processor.GeneratorRepository;
import org.androidtransfuse.processor.GeneratorRepositoryProvider;
import org.androidtransfuse.util.EmptyRResource;
import org.androidtransfuse.util.JavaUtilLogger;
import org.androidtransfuse.util.Logger;
import org.androidtransfuse.util.TestingScope;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.inject.Named;
import javax.lang.model.util.Elements;
import javax.xml.bind.JAXBContext;

@BootstrapModule
@BindInterceptors({
        @BindInterceptor(annotation = Asynchronous.class, interceptor = AsynchronousMethodInterceptor.class),
        @BindInterceptor(annotation = UIThread.class, interceptor = UIThreadMethodInterceptor.class)
})
@DefineScopes({
        @DefineScope(annotation = CodeGenerationScope.class, scope = TestingScope.class),
        @DefineScope(annotation = ConfigurationScope.class, scope = TestingScope.class)
})
@Bindings({
        @Bind(type = ModuleRepository.class, to = InjectionNodeBuilderRepositoryFactory.class),
        @Bind(type = InvocationBuilderStrategy.class, to = DefaultInvocationBuilderStrategy.class),
        @Bind(type = ClassGenerationStrategy.class, to = TransfuseClassGenerationStrategy.class),
        @Bind(type = RResource.class, to = EmptyRResource.class),
        @Bind(type = Elements.class, to = NoOpElements.class),
        @Bind(type = Filer.class, to = NoOpFiler.class),
        @Bind(type = Messager.class, to = NoOpMessager.class)
})
@BindProviders({
        @BindProvider(type = JAXBContext.class, provider = JAXBContextProvider.class),
        @BindProvider(type = VariableExpressionBuilder.class, provider = ExpressionDecoratorFactory.class),
        @BindProvider(type = GeneratorRepository.class, provider = GeneratorRepositoryProvider.class),
        @BindProvider(type = AnalysisRepository.class, provider = AnalysisRepositoryFactory.class)
})
@Install({
        ASTFactory.class,
        InjectionNodeBuilderRepositoryFactory.class,
        VariableExpressionBuilderFactory.class,
        InjectionBuilderContextFactory.class,
        VariableInjectionBuilderFactory.class,
        ComponentBuilderFactory.class,
        GeneratorFactory.class,
        InjectionBuilderContextFactory.class,
        MethodBasedResourceExpressionBuilderAdaptorFactory.class,
        MethodBasedResourceExpressionBuilderFactory.class,
        AnalysisGenerationTransactionProcessorBuilderFactory.class,
        InstantiationStrategyFactory.class
})
public class TestTransfuseAndroidModule {

    private final JCodeModel codeModel = new JCodeModel();

    @Provides
    @ConfigurationScope
    @Named(TransfuseAndroidModule.ORIGINAL_MANIFEST)
    public Manifest getDummyManifest(){
        return new Manifest();
    }

    @Provides
    public Logger getLogger(){
        return new JavaUtilLogger(this);
    }
    @Provides
    public JCodeModel getCodeModel(){
        return codeModel;
    }
}