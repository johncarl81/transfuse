package org.androidtransfuse.processor;

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
    public Exception getError() {
        if (beforeProcessor.getError() != null) {
            return beforeProcessor.getError();
        }
        return afterProcessor.getError();
    }
}
