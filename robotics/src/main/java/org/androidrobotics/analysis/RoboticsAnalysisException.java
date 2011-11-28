package org.androidrobotics.analysis;

/**
 * @author John Ericksen
 */
public class RoboticsAnalysisException extends RuntimeException {
    public RoboticsAnalysisException() {
    }

    public RoboticsAnalysisException(String s) {
        super(s);
    }

    public RoboticsAnalysisException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public RoboticsAnalysisException(Throwable throwable) {
        super(throwable);
    }
}
