package org.androidtransfuse.processor;

import com.google.common.collect.ImmutableSet;

/**
 * @author John Ericksen
 */
public class TransactionProcessorPredefined implements TransactionProcessor {

    private TransactionProcessorPool<Void, Void> transactionProcessor = new TransactionProcessorPool<Void, Void>();

    public TransactionProcessorPredefined(ImmutableSet<Transaction<Void, Void>> transactions) {
        for (Transaction<Void, Void> transaction : transactions) {
            transactionProcessor.submit(transaction);
        }
    }

    @Override
    public void execute() {
        transactionProcessor.execute();
    }

    @Override
    public boolean isComplete() {
        return transactionProcessor.isComplete();
    }

    @Override
    public Exception getError() {
        return transactionProcessor.getError();
    }
}
