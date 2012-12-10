package org.androidtransfuse.processor;

import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.module.ImplementedByTransactionWorker;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class ImplementedByProcessorBuilder implements TransactionProcessorBuilder<Provider<ASTType>, Void> {

    private final ScopedTransactionFactory scopedTransactionFactory;
    private final TransactionProcessorPool<Provider<ASTType>, Void> transactionProcessor;
    private final Provider<ImplementedByTransactionWorker> workerProvider;

    @Inject
    public ImplementedByProcessorBuilder(
            Provider<ImplementedByTransactionWorker> workerProvider,
            ScopedTransactionFactory scopedTransactionFactory) {
        this.scopedTransactionFactory = scopedTransactionFactory;
        this.transactionProcessor = new TransactionProcessorPool<Provider<ASTType>, Void>();
        this.workerProvider = workerProvider;
    }

    @Override
    public void submit(Provider<ASTType> astTypeProvider) {
        transactionProcessor.submit(scopedTransactionFactory.buildTransaction(astTypeProvider, workerProvider));
    }

    @Override
    public TransactionProcessor getTransactionProcessor() {
        return transactionProcessor;
    }
}
