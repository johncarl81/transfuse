package org.androidtransfuse.processor;

import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.config.TransfuseSetupGuiceModule;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class InjectorTransactionProcessorBuilder implements TransactionProcessorBuilder<Provider<ASTType>, JDefinedClass> {

    private final TransactionProcessor<Provider<ASTType>, JDefinedClass> transactionProcessor;
    private final InjectorTransactionFactory injectorTransactionFactory;

    @Inject
    public InjectorTransactionProcessorBuilder(InjectorTransactionFactory injectorTransactionFactory,
                                               @Named(TransfuseSetupGuiceModule.INJECTORS_TRANSACTION_PROCESSOR)
                                               TransactionProcessor<Provider<ASTType>, JDefinedClass> injectorsTransactionWorker) {
        this.transactionProcessor = injectorsTransactionWorker;
        this.injectorTransactionFactory = injectorTransactionFactory;
    }

    @Override
    public void submit(Provider<ASTType> astTypeProvider) {
        transactionProcessor.submit(injectorTransactionFactory.buildTransaction(astTypeProvider));
    }

    @Override
    public TransactionProcessor<Provider<ASTType>, JDefinedClass> getTransactionProcessor() {
        return transactionProcessor;
    }
}
