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
import com.thoughtworks.xstream.XStream;
import org.androidtransfuse.adapter.ASTFactory;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.analysis.module.ModuleRepository;
import org.androidtransfuse.analysis.repository.*;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.aop.AsynchronousMethodInterceptor;
import org.androidtransfuse.aop.UIThreadMethodInterceptor;
import org.androidtransfuse.bootstrap.BootstrapModule;
import org.androidtransfuse.gen.*;
import org.androidtransfuse.gen.invocationBuilder.DefaultInvocationBuilderStrategy;
import org.androidtransfuse.gen.invocationBuilder.InvocationBuilderStrategy;
import org.androidtransfuse.gen.variableBuilder.InjectionNodeBuilder;
import org.androidtransfuse.gen.variableBuilder.VariableInjectionNodeBuilder;
import org.androidtransfuse.gen.variableDecorator.ExpressionDecoratorFactory;
import org.androidtransfuse.gen.variableDecorator.VariableExpressionBuilder;
import org.androidtransfuse.gen.variableDecorator.VariableExpressionBuilderFactory;
import org.androidtransfuse.model.manifest.Manifest;
import org.androidtransfuse.model.r.RResource;
import org.androidtransfuse.processor.*;
import org.androidtransfuse.util.Logger;
import org.androidtransfuse.util.MessagerLogger;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.lang.model.util.Elements;
import java.util.Map;

@BootstrapModule
@BindInterceptors({
        @BindInterceptor(annotation = Asynchronous.class, interceptor = AsynchronousMethodInterceptor.class),
        @BindInterceptor(annotation = UIThread.class, interceptor = UIThreadMethodInterceptor.class)
})
@DefineScopes({
        @DefineScope(annotation = CodeGenerationScope.class, scope = ThreadLocalScope.class),
        @DefineScope(annotation = ConfigurationScope.class, scope = MapScope.class)
})
@Bindings({
        @Bind(type = ModuleRepository.class, to = InjectionNodeBuilderRepositoryFactory.class),
        @Bind(type = InvocationBuilderStrategy.class, to = DefaultInvocationBuilderStrategy.class)
})
@BindProviders({
        @BindProvider(type = XStream.class, provider = XStreamProvider.class),
        @BindProvider(type = VariableExpressionBuilder.class, provider = ExpressionDecoratorFactory.class),
        @BindProvider(type = ProcessingEnvironment.class, provider = ProcessingEnvironmentThrowingProvider.class, scope = Singleton.class),
        @BindProvider(type = RResource.class, provider = RResourceThrowingProvider.class, scope = ConfigurationScope.class),
        @BindProvider(type = Manifest.class, provider = ManifestThrowingProvider.class, scope = ConfigurationScope.class),
        @BindProvider(type = FileProxy.class, provider = FileThrowingProvider.class, scope = ConfigurationScope.class),
        @BindProvider(type = ScopeAspectFactoryRepository.class, provider = ScopeAspectFactoryRepositoryProvider.class),
        @BindProvider(type = GeneratorRepository.class, provider = GeneratorRepositoryProvider.class),
        @BindProvider(type = AnalysisRepository.class, provider = AnalysisRepositoryFactory.class, scope = ConfigurationScope.class),
        @BindProvider(type = JCodeModel.class, provider = JCodeModelProvider.class, scope = CodeGenerationScope.class)
})
@Install({
        ASTFactory.class,
        InjectionNodeBuilderRepositoryFactory.class,
        VariableExpressionBuilderFactory.class,
        InjectionBuilderContextFactory.class})
public class TransfuseAndroidModule {

    public static final String PARCELS_TRANSACTION_WORKER = "parcelsTransactionWorker";
    public static final String PARCEL_TRANSACTION_WORKER = "parcelTransactionWorker";
    public static final String FACTORY_TRANSACTION_WORKER = "factoryTransactionWorker";
    public static final String FACTORIES_TRANSACTION_WORKER = "factoriessTransactionWorker";
    public static final String PACKAGE_HELPER_TRANSACTION_WORKER = "packageHelperTransactionWorker";
    public static final String COMPONENTS_TRANSACTION_WORKER = "componentsTransactionWorker";
    public static final String ORIGINAL_MANIFEST = "originalManifest";
    public static final String DEFAULT_BINDING = "defaultBinding";
    public static final String MANIFEST_FILE = "manifestFile";

    @Provides
    public Elements getElements(ProcessingEnvironment processingEnvironment){
        return new SynchronizedElements(processingEnvironment.getElementUtils());
    }

    @Provides
    public Logger getLogger(ProcessingEnvironment processingEnvironment){
        return new MessagerLogger(processingEnvironment.getMessager());
    }

    @Provides
    public Filer getFiler(ProcessingEnvironment processingEnvironment){
        return new SynchronizedFiler(processingEnvironment.getFiler());
    }

    @Provides
    @Named(TransfuseAndroidModule.DEFAULT_BINDING)
    public InjectionNodeBuilder getDefaultInjectionNodeBuilder(VariableInjectionNodeBuilder variableINB){
        return variableINB;
    }

    public interface ParcelMarkerTransactionWorker<V, R> extends TransactionWorker<V, R>{}

    @Provides
    @Named(PARCEL_TRANSACTION_WORKER)
    public ParcelMarkerTransactionWorker<Provider<ASTType>, JDefinedClass> getParcelTransactionWorker(JCodeModel codeModel,
                                                                                                       FilerSourceCodeWriter codeWriter,
                                                                                                       FilerResourceWriter resourceWriter,
                                                                                                       ParcelTransactionWorker worker) {
        return new CodeGenerationScopedTransactionWorker<Provider<ASTType>, JDefinedClass>(codeModel, codeWriter, resourceWriter, worker);
    }

    public interface FactoryMarkerTransactionWorker<V, R> extends TransactionWorker<V, R>{}

    @Provides
    @Named(FACTORY_TRANSACTION_WORKER)
    public FactoryMarkerTransactionWorker<Provider<ASTType>, JDefinedClass> getFactoryTransactionWorker(JCodeModel codeModel,
                                                                                                        FilerSourceCodeWriter codeWriter,
                                                                                                        FilerResourceWriter resourceWriter,
                                                                                                        FactoryTransactionWorker worker) {
        return new CodeGenerationScopedTransactionWorker<Provider<ASTType>, JDefinedClass>(codeModel, codeWriter, resourceWriter, worker);
    }

    public interface FactoriesMarkerTransactionWorker<V, R> extends TransactionWorker<V, R>{}

    @Provides
    @Named(FACTORIES_TRANSACTION_WORKER)
    public FactoriesMarkerTransactionWorker<Map<Provider<ASTType>, JDefinedClass>, Void> getFactoriesTransactionWorker(JCodeModel codeModel,
                                                                                                                       FilerSourceCodeWriter codeWriter,
                                                                                                                       FilerResourceWriter resourceWriter,
                                                                                                                       FactoriesTransactionWorker worker) {
        return new CodeGenerationScopedTransactionWorker<Map<Provider<ASTType>, JDefinedClass>, Void>(codeModel, codeWriter, resourceWriter, worker);
    }

    public interface ParcelsMarkerTransactionWorker<V, R> extends TransactionWorker<V, R>{}

    @Provides
    @Named(PARCELS_TRANSACTION_WORKER)
    public ParcelsMarkerTransactionWorker<Map<Provider<ASTType>, JDefinedClass>, Void> getParcelsTransactionWorker(JCodeModel codeModel,
                                                                                                                   FilerSourceCodeWriter codeWriter,
                                                                                                                   FilerResourceWriter resourceWriter,
                                                                                                                   ParcelsTransactionWorker worker) {
        return new CodeGenerationScopedTransactionWorker<Map<Provider<ASTType>, JDefinedClass>, Void>(codeModel, codeWriter, resourceWriter, worker);
    }

    public interface PackageHelperMarkerTransactionWorker<V, R> extends TransactionWorker<V, R>{}

    @Provides
    @Named(PACKAGE_HELPER_TRANSACTION_WORKER)
    public PackageHelperMarkerTransactionWorker<Void, Void> getPHTransactionWorker(JCodeModel codeModel,
                                                                FilerSourceCodeWriter codeWriter,
                                                                FilerResourceWriter resourceWriter,
                                                                PackageHelperGeneratorAdapter worker) {
        return new CodeGenerationScopedTransactionWorker<Void, Void>(codeModel, codeWriter, resourceWriter, worker);
    }

    public interface ComponentsMarkerTransactionWorker<V, R> extends TransactionWorker<V, R>{}

    @Provides
    @Named(COMPONENTS_TRANSACTION_WORKER)
    public ComponentsMarkerTransactionWorker<Map<Provider<ASTType>, JDefinedClass>, Void> getComponentsWorker(JCodeModel codeModel,
                                                                                                           FilerSourceCodeWriter codeWriter,
                                                                                                           FilerResourceWriter resourceWriter,
                                                                                                           ComponentsGenerator worker) {
        return new CodeGenerationScopedTransactionWorker<Map<Provider<ASTType>, JDefinedClass>, Void>(codeModel, codeWriter, resourceWriter, worker);
    }

    @Provides
    public FactoryProcessor getFactoryProcessor(FactoryTransactionFactory factoryTransactionFactory,
                                                FactoriesTransactionFactory factoriesTransactionFactory) {
        TransactionProcessorPool<Provider<ASTType>, JDefinedClass> factoryProcessor =
                new TransactionProcessorPool<Provider<ASTType>, JDefinedClass>();
        TransactionProcessorPool<Map<Provider<ASTType>, JDefinedClass>, Void> factoriesProcessor =
                new TransactionProcessorPool<Map<Provider<ASTType>, JDefinedClass>, Void>();

        TransactionProcessor processor =
                new TransactionProcessorChannel<Provider<ASTType>, JDefinedClass, Void>(factoryProcessor, factoriesProcessor, factoriesTransactionFactory);

        return new FactoryProcessor(processor, factoryProcessor, factoryTransactionFactory);
    }

    @Provides
    public ParcelProcessor getParcelProcessor(ParcelTransactionFactory parcelTransactionFactory,
                                              ParcelsTransactionFactory parcelsTransactionFactory) {

        TransactionProcessorPool<Provider<ASTType>, JDefinedClass> parcelProcessor =
                new TransactionProcessorPool<Provider<ASTType>, JDefinedClass>();
        TransactionProcessorPool<Map<Provider<ASTType>, JDefinedClass>, Void> parcelsProcessor =
                new TransactionProcessorPool<Map<Provider<ASTType>, JDefinedClass>, Void>();

        TransactionProcessor processor =
                new TransactionProcessorChannel<Provider<ASTType>, JDefinedClass, Void>(parcelProcessor, parcelsProcessor, parcelsTransactionFactory);

        return new ParcelProcessor(processor, parcelProcessor, parcelTransactionFactory);
    }
}