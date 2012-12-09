package org.androidtransfuse.analysis;

import com.google.common.collect.ImmutableSet;

/**
 * @author John Ericksen
 */
public class TransfuseAnalysisException extends RuntimeException {
    public TransfuseAnalysisException() {
    }

    public TransfuseAnalysisException(String message) {
        super(message);
    }

    public TransfuseAnalysisException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public TransfuseAnalysisException(Throwable throwable) {
        super(throwable);
    }

    public TransfuseAnalysisException(String message, ImmutableSet<Exception> errors) {
        //todo: print out all errors
        super(message, errors.isEmpty() ? null : errors.iterator().next());
    }
}
