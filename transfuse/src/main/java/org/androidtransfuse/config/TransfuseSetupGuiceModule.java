/**
 * Copyright 2012 John Ericksen
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
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import com.google.inject.util.Providers;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.thoughtworks.xstream.XStream;
import org.androidtransfuse.analysis.adapter.ASTFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.module.BindingConfigurationFactory;
import org.androidtransfuse.gen.FilerResourceWriter;
import org.androidtransfuse.gen.FilerSourceCodeWriter;
import org.androidtransfuse.gen.invocationBuilder.PackageHelperGenerator;
import org.androidtransfuse.processor.*;
import org.androidtransfuse.util.Logger;

import javax.annotation.processing.Filer;
import javax.inject.Named;
import javax.inject.Provider;
import javax.lang.model.util.Elements;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class TransfuseSetupGuiceModule extends AbstractModule {

    public static final String PARCELS_TRANSACTION_WORKER = "parcelsTransactionWorker";
    public static final String PARCEL_TRANSACTION_WORKER = "parcelTransactionWorker";
    public static final String INJECTOR_TRANSACTION_WORKER = "injectorTransactionWorker";
    public static final String INJECTORS_TRANSACTION_WORKER = "injectorsTransactionWorker";
    public static final String CODE_GENERATION_SCOPE = "codeGenerationScope";
    public static final String PACKAGE_HELPER_TRANSACTION_WORKER = "packageHelperTransactionWorker";

    private final Logger logger;
    private final Filer filer;
    private final Elements elements;
    private final EnterableScope codeGenerationScope;

    public TransfuseSetupGuiceModule(Logger logger, Filer filer, Elements elementUtils, EnterableScope codeGenerationScope) {
        this.logger = logger;
        this.filer = filer;
        this.elements = elementUtils;
        this.codeGenerationScope = codeGenerationScope;
    }

    @Override
    protected void configure() {
        FactoryModuleBuilder factoryModuleBuilder = new FactoryModuleBuilder();

        install(factoryModuleBuilder.build(ASTFactory.class));
        install(factoryModuleBuilder.build(AnalysisGenerationTransactionProcessorBuilderFactory.class));
        install(factoryModuleBuilder.build(BindingConfigurationFactory.class));

        bind(Logger.class).toInstance(logger);
        bind(XStream.class).toProvider(XStreamProvider.class);
        bind(Filer.class).toInstance(filer);
        bind(Elements.class).toInstance(elements);

        bindScope(CodeGenerationScope.class, codeGenerationScope);
        bind(EnterableScope.class).annotatedWith(Names.named(CODE_GENERATION_SCOPE)).toInstance(codeGenerationScope);

        bind(JCodeModel.class).toProvider(Providers.guicify(new JCodeModelProvider())).in(CodeGenerationScope.class);

        bind(FilerSourceCodeWriter.class).in(CodeGenerationScope.class);
        bind(FilerResourceWriter.class).in(CodeGenerationScope.class);
    }

    private static class JCodeModelProvider implements Provider<JCodeModel> {
        @Override
        public JCodeModel get() {
            return new JCodeModel();
        }
    }

    @Provides
    @Named(PARCEL_TRANSACTION_WORKER)
    public TransactionWorker<Provider<ASTType>, JDefinedClass> getParcelTransactionWorker(JCodeModel codeModel,
                                                                                          FilerSourceCodeWriter codeWriter,
                                                                                          FilerResourceWriter resourceWriter,
                                                                                          ParcelTransactionWorker worker) {
        return new CodeGenerationScopedTransactionWorker<Provider<ASTType>, JDefinedClass>(codeModel, codeWriter, resourceWriter, worker);
    }

    @Provides
    @Named(INJECTOR_TRANSACTION_WORKER)
    public TransactionWorker<Provider<ASTType>, JDefinedClass> getInjectorTransactionWorker(JCodeModel codeModel,
                                                                                            FilerSourceCodeWriter codeWriter,
                                                                                            FilerResourceWriter resourceWriter,
                                                                                            InjectorTransactionWorker worker) {
        return new CodeGenerationScopedTransactionWorker<Provider<ASTType>, JDefinedClass>(codeModel, codeWriter, resourceWriter, worker);
    }

    @Provides
    @Named(INJECTORS_TRANSACTION_WORKER)
    public TransactionWorker<Map<Provider<ASTType>, JDefinedClass>, Void> getInjectorsTransactionWorker(JCodeModel codeModel,
                                                                                                        FilerSourceCodeWriter codeWriter,
                                                                                                        FilerResourceWriter resourceWriter,
                                                                                                        InjectorsTransactionWorker worker) {
        return new CodeGenerationScopedTransactionWorker<Map<Provider<ASTType>, JDefinedClass>, Void>(codeModel, codeWriter, resourceWriter, worker);
    }

    @Provides
    @Named(PARCELS_TRANSACTION_WORKER)
    public TransactionWorker<Map<Provider<ASTType>, JDefinedClass>, Void> getParcelsTransactionWorker(JCodeModel codeModel,
                                                                                                      FilerSourceCodeWriter codeWriter,
                                                                                                      FilerResourceWriter resourceWriter,
                                                                                                      ParcelsTransactionWorker worker) {
        return new CodeGenerationScopedTransactionWorker<Map<Provider<ASTType>, JDefinedClass>, Void>(codeModel, codeWriter, resourceWriter, worker);
    }

    @Provides
    @Named(PACKAGE_HELPER_TRANSACTION_WORKER)
    public TransactionWorker<Void, Void> getPHTransactionWorker(JCodeModel codeModel,
                                                                FilerSourceCodeWriter codeWriter,
                                                                FilerResourceWriter resourceWriter,
                                                                PackageHelperGenerator worker) {
        return new CodeGenerationScopedTransactionWorker<Void, Void>(codeModel, codeWriter, resourceWriter, worker);
    }

    @Provides
    public InjectorProcessor getInjectorProcessor(InjectorTransactionFactory injectorTransactionFactory,
                                                  InjectorsTransactionFactory injectorsTransactionFactory) {
        TransactionProcessorPool<Provider<ASTType>, JDefinedClass> injectorProcessor =
                new TransactionProcessorPool<Provider<ASTType>, JDefinedClass>();
        TransactionProcessorPool<Map<Provider<ASTType>, JDefinedClass>, Void> parcelsProcessor =
                new TransactionProcessorPool<Map<Provider<ASTType>, JDefinedClass>, Void>();

        TransactionProcessor processor =
                new TransactionProcessorChannel<Provider<ASTType>, JDefinedClass, Void>(injectorProcessor, parcelsProcessor, injectorsTransactionFactory);

        return new InjectorProcessor(processor, injectorProcessor, injectorTransactionFactory);
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
