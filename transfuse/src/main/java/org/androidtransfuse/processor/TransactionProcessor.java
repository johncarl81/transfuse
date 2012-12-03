package org.androidtransfuse.processor;

/**
 * @author John Ericksen
 */
public interface TransactionProcessor {

    void execute();

    public boolean isComplete();

    Exception getError();
}
