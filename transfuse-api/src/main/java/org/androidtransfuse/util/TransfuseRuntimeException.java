package org.androidtransfuse.util;

/**
 * @author John Ericksen
 */
public class TransfuseRuntimeException extends RuntimeException {
    public TransfuseRuntimeException(String s, Exception e) {
        super(s,e);
    }
}
