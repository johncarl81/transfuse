package org.androidrobotics.util;

/**
 * @author John Ericksen
 */
public class JavaUtilLogger implements Logger {

    private java.util.logging.Logger logger;

    public JavaUtilLogger(Object targetInstance) {
        this.logger = java.util.logging.Logger.getLogger(targetInstance.getClass().getCanonicalName());
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

    @Override
    public void error(String s, Throwable e) {
        logger.throwing(s, e.getMessage(), e);
    }
}
