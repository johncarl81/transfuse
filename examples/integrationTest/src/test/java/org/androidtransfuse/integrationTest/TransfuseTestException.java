package org.androidtransfuse.integrationTest;

/**
 * @author John Ericksen
 */
public class TransfuseTestException extends RuntimeException {
    public TransfuseTestException(String s) {
        super(s);
    }

    public TransfuseTestException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
