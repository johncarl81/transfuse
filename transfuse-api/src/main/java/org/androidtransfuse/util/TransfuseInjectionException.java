package org.androidtransfuse.util;

/**
 * @author John Ericksen
 */
public class TransfuseInjectionException extends RuntimeException {
    public TransfuseInjectionException() {
    }

    public TransfuseInjectionException(String detailMessage) {
        super(detailMessage);
    }

    public TransfuseInjectionException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public TransfuseInjectionException(Throwable throwable) {
        super(throwable);
    }
}
