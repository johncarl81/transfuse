package org.androidtransfuse.analysis.adapter;

/**
 * Abstract Syntax Tree Annotation
 *
 * @author John Ericksen
 */
public interface ASTAnnotation {

    /**
     * Getter for a given annotation property
     *
     * @param name
     * @param type
     * @param <T>
     * @return annotation property identified by name with the given type
     */
    <T> T getProperty(String name, Class<T> type);

    /**
     * Getter for the name of the current annotaion
     *
     * @return annotation name
     */
    ASTType getASTType();
}
