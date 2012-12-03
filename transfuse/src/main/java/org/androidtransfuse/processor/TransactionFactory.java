package org.androidtransfuse.processor;

/**
 * @author John Ericksen
 */
public interface TransactionFactory<V, R> {

    Transaction<V, R> buildTransaction(V value);
}
