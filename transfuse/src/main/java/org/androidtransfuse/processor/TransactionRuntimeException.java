package org.androidtransfuse.processor;

/**
 * Exception to be thrown during the execution of a Transaction which will quit the transaction and flag it to be
 * retried.
 *
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
