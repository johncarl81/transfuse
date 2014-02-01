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
import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.CodeGenerationScope;
import org.androidtransfuse.ConfigurationScope;
import org.androidtransfuse.adapter.ASTFactory;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.analysis.module.ModuleRepository;
import org.androidtransfuse.analysis.repository.AnalysisRepository;
import org.androidtransfuse.analysis.repository.AnalysisRepositoryFactory;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepositoryFactory;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.bootstrap.BootstrapModule;
import org.androidtransfuse.bootstrap.Namespace;
import org.androidtransfuse.gen.ClassGenerationStrategy;
import org.androidtransfuse.gen.InjectionBuilderContextFactory;
import org.androidtransfuse.gen.InstantiationStrategyFactory;
import org.androidtransfuse.gen.TransfuseClassGenerationStrategy;
import org.androidtransfuse.gen.invocationBuilder.DefaultInvocationBuilderStrategy;
import org.androidtransfuse.gen.invocationBuilder.InvocationBuilderStrategy;
import org.androidtransfuse.gen.variableDecorator.ExpressionDecoratorFactory;
import org.androidtransfuse.gen.variableDecorator.VariableExpressionBuilder;
import org.androidtransfuse.gen.variableDecorator.VariableExpressionBuilderFactory;
import org.androidtransfuse.model.manifest.Manifest;
import org.androidtransfuse.model.r.RResource;
import org.androidtransfuse.processor.*;
import org.androidtransfuse.transaction.ScopedTransactionBuilder;
import org.androidtransfuse.transaction.TransactionProcessor;
import org.androidtransfuse.transaction.TransactionProcessorChannel;
import org.androidtransfuse.transaction.TransactionProcessorPool;
import org.androidtransfuse.util.Logger;
import org.androidtransfuse.util.ManifestLocator;
import org.androidtransfuse.util.MessagerLogger;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.lang.model.util.Elements;
import javax.xml.bind.JAXBContext;
import java.io.File;
import java.util.Map;

@BootstrapModule
@DefineScopes({
        @DefineScope(annotation = CodeGenerationScope.class, scope = ThreadLocalScope.class),
        @DefineScope(annotation = ConfigurationScope.class, scope = MapScope.class)
})
@Bindings({
        @Bind(type = ModuleRepository.class, to = InjectionNodeBuilderRepositoryFactory.class),
        @Bind(type = InvocationBuilderStrategy.class, to = DefaultInvocationBuilderStrategy.class),
        @Bind(type = ClassGenerationStrategy.class, to = TransfuseClassGenerationStrategy.class)
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
        InstantiationStrategyFactory.class})
@Namespace("Transfuse")
public class TransfuseAndroidModule {

    public static final String ORIGINAL_MANIFEST = "originalManifest";
    public static final String MANIFEST_FILE = "manifestFile";

    @Provides
    @CodeGenerationScope
    public JCodeModel getJCodeModel(){
        return new JCodeModel();
    }

    @Provides
    @Singleton
    public Elements getElements(ProcessingEnvironment processingEnvironment){
        return processingEnvironment.getElementUtils();
    }

    @Provides
    @Singleton
    public Messager getMessenger(ProcessingEnvironment processingEnvironment){
        return processingEnvironment.getMessager();
    }

    @Provides
    @Singleton
    public Logger getLogger(ProcessingEnvironment processingEnvironment){
        return new MessagerLogger(processingEnvironment.getMessager());
    }

    @Provides
    @Singleton
    public Filer getFiler(ProcessingEnvironment processingEnvironment){
        return new SynchronizedFiler(processingEnvironment.getFiler());
    }

    @Provides
    @Named(ManifestLocator.ANDROID_MANIFEST_FILE_OPTION)
    public String getManifestFileLocation(ProcessingEnvironment processingEnvironment){
        return processingEnvironment.getOptions().get(ManifestLocator.ANDROID_MANIFEST_FILE_OPTION);
    }

    @Provides
    @Named(GenerateModuleProcessor.MANIFEST_PROCESSING_OPTION)
    public String getManifestProcessing(ProcessingEnvironment processingEnvironment){
        return processingEnvironment.getOptions().get(GenerateModuleProcessor.MANIFEST_PROCESSING_OPTION);
    }

    @Provides
    @Singleton
    public ProcessingEnvironment getProcessingEnvironment(){
        throw new OutOfScopeException("Expected seeded object, unable to construct directly.");
    }

    @Provides
    @ConfigurationScope
    public RResource getRResource(){
        throw new OutOfScopeException("Expected seeded object, unable to construct directly.");
    }

    @Provides
    @ConfigurationScope
    @Named(MANIFEST_FILE)
    public File getManifestFile(){
        throw new OutOfScopeException("Expected seeded object, unable to construct directly.");
    }

    @Provides
    @ConfigurationScope
    @Named(ORIGINAL_MANIFEST)
    public Manifest getManifest(){
        throw new OutOfScopeException("Expected seeded object, unable to construct directly.");
    }

    @Provides
    public FactoryProcessor getFactoryProcessor(Provider<FactoryTransactionWorker> factoryTransactionWorkerProvider,
                                                Provider<FactoriesTransactionWorker> factoriesTransactionWorkerProvider,
                                                ScopedTransactionBuilder scopedTransactionBuilder) {
        TransactionProcessorPool<Provider<ASTType>, JDefinedClass> factoryProcessor =
                new TransactionProcessorPool<Provider<ASTType>, JDefinedClass>();
        TransactionProcessorPool<Map<Provider<ASTType>, JDefinedClass>, Void> factoriesProcessor =
                new TransactionProcessorPool<Map<Provider<ASTType>, JDefinedClass>, Void>();

        TransactionProcessor processor =
                new TransactionProcessorChannel<Provider<ASTType>, JDefinedClass, Void>(
                        factoryProcessor,
                        factoriesProcessor,
                        scopedTransactionBuilder.buildFactory(factoriesTransactionWorkerProvider));

        return new FactoryProcessor(processor, factoryProcessor, scopedTransactionBuilder.buildFactory(factoryTransactionWorkerProvider));
    }
}