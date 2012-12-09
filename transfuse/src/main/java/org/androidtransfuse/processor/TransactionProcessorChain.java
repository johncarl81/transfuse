package org.androidtransfuse.processor;

import com.google.common.collect.ImmutableSet;

/**
 * @author John Ericksen
 */
public class TransactionProcessorChain implements TransactionProcessor {

    private final TransactionProcessor beforeProcessor;
    private final TransactionProcessor afterProcessor;

    public TransactionProcessorChain(TransactionProcessor beforeProcessor, TransactionProcessor afterProcessor) {
        this.beforeProcessor = beforeProcessor;
        this.afterProcessor = afterProcessor;
    }

    @Override
    public void execute() {
        beforeProcessor.execute();

        if (beforeProcessor.isComplete() && !afterProcessor.isComplete()) {
            afterProcessor.execute();
        }
    }

    @Override
    public boolean isComplete() {
        return beforeProcessor.isComplete() && afterProcessor.isComplete();
    }

    @Override
    public ImmutableSet<Exception> getErrors() {
        ImmutableSet.Builder<Exception> exceptions = ImmutableSet.builder();
        exceptions.addAll(beforeProcessor.getErrors());
        exceptions.addAll(afterProcessor.getErrors());

        return exceptions.build();
    }
}
