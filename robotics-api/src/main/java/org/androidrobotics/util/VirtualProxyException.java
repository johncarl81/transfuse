package org.androidrobotics.util;

/**
 * @author John Ericksen
 */
public class VirtualProxyException extends RuntimeException {

    public VirtualProxyException() {
    }

    public VirtualProxyException(String s) {
        super(s);
    }

    public VirtualProxyException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public VirtualProxyException(Throwable throwable) {
        super(throwable);
    }
}
