package org.androidrobotics.util;

/**
 * @author John Ericksen
 */
public class JavaUtilLogger implements Logger {

    private java.util.logging.Logger logger;

    public JavaUtilLogger(java.util.logging.Logger logger) {
        this.logger = logger;
    }

    @Override
    public void info(String value) {
        logger.info(value);
    }

    @Override
    public void warning(String value) {
        logger.warning(value);
    }

    @Override
    public void error(String value) {
        logger.severe(value);
    }
}
