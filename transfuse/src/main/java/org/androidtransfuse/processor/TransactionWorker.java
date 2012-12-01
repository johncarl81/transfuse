package org.androidtransfuse.processor;

/**
 * @author John Ericksen
 */
public interface TransactionWorker<V, R> {

    boolean isComplete();

    R run(V value);

    Exception getError();
}
