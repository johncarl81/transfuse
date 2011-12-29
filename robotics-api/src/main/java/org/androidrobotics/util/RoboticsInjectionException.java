package org.androidrobotics.util;

/**
 * @author John Ericksen
 */
public class RoboticsInjectionException extends RuntimeException {
    public RoboticsInjectionException() {
    }

    public RoboticsInjectionException(String detailMessage) {
        super(detailMessage);
    }

    public RoboticsInjectionException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public RoboticsInjectionException(Throwable throwable) {
        super(throwable);
    }
}
