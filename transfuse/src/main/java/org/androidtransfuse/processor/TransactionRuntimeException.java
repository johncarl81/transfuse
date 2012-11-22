package org.androidtransfuse.processor;

/**
 * @author John Ericksen
 */
public class TransactionRuntimeException extends RuntimeException {

    public TransactionRuntimeException() {
    }

    public TransactionRuntimeException(String message) {
        super(message);
    }

    public TransactionRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionRuntimeException(Throwable cause) {
        super(cause);
    }
}
