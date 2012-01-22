package org.androidrobotics.analysis.adapter;

/**
 * @author John Ericksen
 */
public interface ASTAnnotation {

    <T> T getProperty(String value, Class<T> type);

    String getName();
}
