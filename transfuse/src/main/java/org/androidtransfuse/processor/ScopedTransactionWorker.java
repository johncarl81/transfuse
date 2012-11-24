package org.androidtransfuse.processor;

import org.androidtransfuse.config.EnterableScope;

import javax.inject.Provider;

/**
 * Executes the given instance of a TransactionWorker with in a code generation scoped transaction.  A unique instance
 * of CodeModel is supplied in this scope which is used to generate code at the end of the transaction.  If a
 * TransactionRuntimeException is thrown this transaction will effectively reset and allow the TransactionWorker
 * to be retried at a later code generation round.
 *
 * @author John Ericksen
 */
public class ScopedTransactionWorker<T extends TransactionWorker<V, R>, V, R> implements TransactionWorker<V, R> {

    private final EnterableScope simpleScope;
    private final Provider<T> workerProvider;
    private T scoped = null;
    private boolean complete = false;
    private Exception error;

    public ScopedTransactionWorker(EnterableScope simpleScope, Provider<T> workerProvider) {
        this.simpleScope = simpleScope;
        this.workerProvider = workerProvider;
    }

    @Override
    public boolean isComplete() {
        return complete && scoped != null && scoped.isComplete();
    }

    @Override
    public R runScoped(V value) {

        try {
            simpleScope.enter();

            scoped = workerProvider.get();
            R result = scoped.runScoped(value);

            complete = true;
            return result;

        } catch (TransactionRuntimeException re) {
            //retry later
            error = re;
            complete = false;
        } finally {
            simpleScope.exit();
        }
        return null;
    }

    public Exception getError() {
        return error;
    }
}
