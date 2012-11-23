package org.androidtransfuse.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.sun.codemodel.JDefinedClass;
import com.thoughtworks.xstream.XStream;
import org.androidtransfuse.analysis.adapter.ASTFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.processor.ParcelsTransactionWorker;
import org.androidtransfuse.processor.ScopedTransactionWorker;
import org.androidtransfuse.processor.TransactionProcessor;
import org.androidtransfuse.util.Logger;

import javax.annotation.processing.Filer;
import javax.inject.Named;
import javax.lang.model.util.Elements;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class TransfuseSetupGuiceModule extends AbstractModule {


    public static final String PARCEL_TRANSACTION_PROCESSOR = "parcelTransactionProcessor";

    private final Logger logger;
    private final Filer filer;
    private final Elements elements;

    public TransfuseSetupGuiceModule(Logger logger, Filer filer, Elements elementUtils) {
        this.logger = logger;
        this.filer = filer;
        this.elements = elementUtils;
    }

    @Override
    protected void configure() {
        FactoryModuleBuilder factoryModuleBuilder = new FactoryModuleBuilder();

        install(factoryModuleBuilder.build(ASTFactory.class));

        bind(Logger.class).toInstance(logger);
        bind(XStream.class).toProvider(XStreamProvider.class);
        bind(Filer.class).toInstance(filer);
        bind(Elements.class).toInstance(elements);
    }

    @Provides
    @Named(PARCEL_TRANSACTION_PROCESSOR)
    public TransactionProcessor<ASTType, JDefinedClass> getParcelTransactionProcessor() {
        return new TransactionProcessor<ASTType, JDefinedClass>(
                new ScopedTransactionWorker<ParcelsTransactionWorker, Map<ASTType, JDefinedClass>, Void>(
                        ParcelsTransactionWorker.class));
    }
}
