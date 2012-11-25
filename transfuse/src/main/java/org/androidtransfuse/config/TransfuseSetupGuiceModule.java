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
import org.androidtransfuse.gen.FilerSourceCodeWriter;
import org.androidtransfuse.gen.ResourceCodeWriter;
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

    public static final String PARCELS_TRANSACTION_PROCESSOR = "parcelsTransactionProcessor";
    public static final String PARCELS_TRANSACTION_WORKER = "parcelsTransactionWorker";
    public static final String PARCEL_TRANSACTION_WORKER = "parcelTransactionWorker";
    public static final String CODE_GENERATION_SCOPE = "codeGenerationScope";

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

        bind(Logger.class).toInstance(logger);
        bind(XStream.class).toProvider(XStreamProvider.class);
        bind(Filer.class).toInstance(filer);
        bind(Elements.class).toInstance(elements);

        bindScope(CodeGenerationScope.class, codeGenerationScope);
        bind(EnterableScope.class).annotatedWith(Names.named(CODE_GENERATION_SCOPE)).toInstance(codeGenerationScope);

        bind(JCodeModel.class).toProvider(Providers.guicify(new JCodeModelProvider())).in(CodeGenerationScope.class);
    }

    private static class JCodeModelProvider implements Provider<JCodeModel> {
        @Override
        public JCodeModel get() {
            return new JCodeModel();
        }
    }

    @Provides
    @Named(PARCELS_TRANSACTION_WORKER)
    public TransactionWorker<Map<Provider<ASTType>, JDefinedClass>, Void> getParcelsTransactionWorker(JCodeModel codeModel,
                                                                                                      FilerSourceCodeWriter codeWriter,
                                                                                                      ResourceCodeWriter resourceWriter,
                                                                                                      ParcelsTransactionWorker worker) {
        return new CodeGenerationScopedTransactionWorker<Map<Provider<ASTType>, JDefinedClass>, Void>(codeModel, codeWriter, resourceWriter, worker);
    }

    @Provides
    @Named(PARCEL_TRANSACTION_WORKER)
    public TransactionWorker<Provider<ASTType>, JDefinedClass> getParcelTransactionWorker(JCodeModel codeModel,
                                                                                          FilerSourceCodeWriter codeWriter,
                                                                                          ResourceCodeWriter resourceWriter,
                                                                                          ParcelTransactionWorker worker) {
        return new CodeGenerationScopedTransactionWorker<Provider<ASTType>, JDefinedClass>(codeModel, codeWriter, resourceWriter, worker);
    }

    @Provides
    @Named(PARCELS_TRANSACTION_PROCESSOR)
    public TransactionProcessor<Provider<ASTType>, JDefinedClass> getParcelTransactionProcessor(
            @Named(TransfuseSetupGuiceModule.CODE_GENERATION_SCOPE) EnterableScope scope,
            @Named(PARCELS_TRANSACTION_WORKER) Provider<TransactionWorker<Map<Provider<ASTType>, JDefinedClass>, Void>> worker) {

        return new TransactionProcessor<Provider<ASTType>, JDefinedClass>(
                new ScopedTransactionWorker<TransactionWorker<Map<Provider<ASTType>, JDefinedClass>, Void>, Map<Provider<ASTType>, JDefinedClass>, Void>
                        (scope, worker));
    }
}
