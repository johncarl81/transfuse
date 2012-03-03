package org.androidtransfuse.example.simple;

/**
 * @author John Ericksen
 */
public class TransfuseTestException extends RuntimeException {
    public TransfuseTestException(String s) {
        super(s);
    }

    public TransfuseTestException(String message, Throwable cause) {
        super(message, cause);
    }
}
