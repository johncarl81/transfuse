package org.androidtransfuse.processor;

/**
 * @author John Ericksen
 */
public interface TransactionWorker<V, R> {

    boolean isComplete();

    R runScoped(V value);
}
