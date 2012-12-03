package org.androidtransfuse.processor;

import java.util.Map;

/**
 * @author John Ericksen
 */
public class TransactionProcessorChannel<V, R, R2> implements TransactionProcessor {

    private TransactionProcessorPool<V, R> completionProcessor;
    private TransactionProcessorPool<Map<V, R>, R2> afterCompletionProcessor;
    private TransactionFactory<Map<V, R>, R2> completionTransactionFactory;

    public TransactionProcessorChannel(TransactionProcessorPool<V, R> completionProcessor,
                                       TransactionProcessorPool<Map<V, R>, R2> afterCompletionProcessor,
                                       TransactionFactory<Map<V, R>, R2> completionTransactionFactory) {
        this.completionProcessor = completionProcessor;
        this.afterCompletionProcessor = afterCompletionProcessor;
        this.completionTransactionFactory = completionTransactionFactory;
    }

    @Override
    public void execute() {

        boolean workToComplete = !completionProcessor.isComplete();
        completionProcessor.execute();

        if (workToComplete && completionProcessor.isComplete()) {
            Transaction<Map<V, R>, R2> completionTransaction = completionTransactionFactory.buildTransaction(completionProcessor.getResults());
            afterCompletionProcessor.submit(completionTransaction);
            afterCompletionProcessor.execute();
        }
    }

    @Override
    public boolean isComplete() {
        return completionProcessor.isComplete() && afterCompletionProcessor.isComplete();
    }

    @Override
    public Exception getError() {
        if (completionProcessor.getError() == null) {
            return afterCompletionProcessor.getError();
        }
        return completionProcessor.getError();
    }
}
