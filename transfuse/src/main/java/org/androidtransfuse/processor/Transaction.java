package org.androidtransfuse.processor;

/**
 * Transaction interface used to encapsulate a set of work.  Encapsulating
 *
 * @author John Ericksen
 */
public interface Transaction<V, R> extends Runnable {

    /**
     * Determines the status of the given work.
     *
     * @return complete
     */
    boolean isComplete();

    /**
     * Gets the value provided to this Transaction to run on.
     *
     * @return value
     */
    V getValue();

    /**
     * Gets the result (once complete) generated during the call to run().
     *
     * @return result
     */
    R getResult();
}
