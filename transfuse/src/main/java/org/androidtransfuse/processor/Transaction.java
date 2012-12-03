package org.androidtransfuse.processor;

/**
 * Transaction used to encapsulate work.
 *
 * @author John Ericksen
 */
public class Transaction<V, R> implements Runnable {

    private final TransactionWorker<V, R> worker;
    private final V value;
    private R result = null;
    private boolean complete = false;

    public Transaction(TransactionWorker<V, R> worker) {
        this(null, worker);
    }

    public Transaction(V value, TransactionWorker<V, R> worker) {
        this.value = value;
        this.worker = worker;
    }

    /**
     * Determines the status of the given work.
     *
     * @return complete
     */
    public boolean isComplete() {
        return complete && worker != null && worker.isComplete();
    }

    /**
     * Gets the value provided to this Transaction to run on.
     *
     * @return value
     */
    public V getValue() {
        return value;
    }

    /**
     * Gets the result (once complete) generated during the call to run().
     *
     * @return result
     */
    public R getResult() {
        return result;
    }

    /**
     * Returns the exception the caused the transaction to abort
     *
     * @return exception
     */
    public Exception getError() {
        return worker == null ? null : worker.getError();
    }

    @Override
    public void run() {
        result = worker.run(value);
        complete = true;
    }
}
