package org.androidtransfuse.processor;

/**
 * @author John Ericksen
 */
public abstract class AbstractCompletionTransactionWorker<V, R> implements TransactionWorker<V, R> {

    private boolean complete = false;

    @Override
    public boolean isComplete() {
        return complete;
    }

    @Override
    public R run(V value) {
        R result = innerRun(value);

        complete = true;
        return result;
    }

    public abstract R innerRun(V value);

    @Override
    public Exception getError() {
        return null;
    }
}
