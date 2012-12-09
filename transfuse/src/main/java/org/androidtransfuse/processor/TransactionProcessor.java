package org.androidtransfuse.processor;

import com.google.common.collect.ImmutableSet;

/**
 * @author John Ericksen
 */
public interface TransactionProcessor {

    void execute();

    boolean isComplete();

    ImmutableSet<Exception> getErrors();
}
