package org.androidtransfuse.processor;

/**
 * @author John Ericksen
 */
public interface TransactionProcessor {

    void execute();

    boolean isComplete();

    Exception getError();
}
