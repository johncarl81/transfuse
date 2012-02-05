package org.androidtransfuse.analysis.adapter;

/**
 * @author John Ericksen
 */
public interface ASTAnnotation {

    <T> T getProperty(String value, Class<T> type);

    String getName();
}
