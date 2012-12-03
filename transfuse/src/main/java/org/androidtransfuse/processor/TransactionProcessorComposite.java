package org.androidtransfuse.processor;

import com.google.common.collect.ImmutableSet;

/**
 * @author John Ericksen
 */
public class TransactionProcessorComposite implements TransactionProcessor {

    private final ImmutableSet<TransactionProcessor> processors;

    public TransactionProcessorComposite(ImmutableSet<TransactionProcessor> processors) {
        this.processors = processors;
    }

    @Override
    public void execute() {
        for (TransactionProcessor processor : processors) {
            processor.execute();
        }
    }

    @Override
    public boolean isComplete() {
        for (TransactionProcessor processor : processors) {
            if (!processor.isComplete()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Exception getError() {
        for (TransactionProcessor processor : processors) {
            if (processor.getError() != null) {
                //todo: multiple errors
                return processor.getError();
            }
        }
        return null;
    }
}
