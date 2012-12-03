package org.androidtransfuse.processor;

import com.google.common.collect.ImmutableSet;

/**
 * @author John Ericksen
 */
public class TransactionProcessorPredefined<V, R> implements TransactionProcessor {

    private TransactionProcessorPool<V, R> transactionProcessor = new TransactionProcessorPool<V, R>();

    public TransactionProcessorPredefined(ImmutableSet<Transaction<V, R>> transactions) {
        for (Transaction<V, R> transaction : transactions) {
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
