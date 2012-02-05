package org.androidtransfuse.util;

/**
 * @author John Ericksen
 */
public interface Logger {
    void info(String value);

    void warning(String value);

    void error(String value);

    void error(String s, Throwable e);
}
