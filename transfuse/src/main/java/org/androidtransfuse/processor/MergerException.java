package org.androidtransfuse.processor;

/**
 * @author John Ericksen
 */
public class MergerException extends Exception {

    public MergerException() {
    }

    public MergerException(String s) {
        super(s);
    }

    public MergerException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public MergerException(Throwable throwable) {
        super(throwable);
    }
}
