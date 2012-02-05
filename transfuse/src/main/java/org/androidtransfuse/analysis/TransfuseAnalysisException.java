package org.androidtransfuse.analysis;

/**
 * @author John Ericksen
 */
public class TransfuseAnalysisException extends RuntimeException {
    public TransfuseAnalysisException() {
    }

    public TransfuseAnalysisException(String s) {
        super(s);
    }

    public TransfuseAnalysisException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public TransfuseAnalysisException(Throwable throwable) {
        super(throwable);
    }
}
