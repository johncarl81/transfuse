package org.androidtransfuse.processor;

/**
 * @author John Ericksen
 */
public interface TransactionProcessorBuilder<T, R> {

    void submit(T astTypeProvider);

    TransactionProcessor getTransactionProcessor();
}
