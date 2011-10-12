package org.androidrobotics.util;

/**
 * @author John Ericksen
 */
public class RoboticsGenerationException extends RuntimeException {
    public RoboticsGenerationException() {
    }

    public RoboticsGenerationException(String message) {
        super(message);
    }

    public RoboticsGenerationException(String message, Throwable cause) {
        super(message, cause);
    }

    public RoboticsGenerationException(Throwable cause) {
        super(cause);
    }
}
