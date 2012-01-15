package org.androidrobotics.analysis.adapter;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * Base class defining shared Abstract Syntax Tree elements
 *
 * @author John Ericksen
 */
public interface ASTBase {

    /**
     * Determines if the current element is annotated with the given annotation class
     *
     * @param annotation key
     * @return annotated conditional
     */
    boolean isAnnotated(Class<? extends Annotation> annotation);


    /**
     * Supplies the set of annotations
     *
     * @return ast annotation list
     */
    Collection<ASTAnnotation> getAnnotations();

    /**
     * Supplies the given annotation instance from the given annotation class key
     *
     * @param annotation key
     * @param <A>        annotation type
     * @return annotation instance
     */
    <A extends Annotation> A getAnnotation(Class<A> annotation);

    /**
     * Supplies the name of the current tree node
     *
     * @return name of the current node
     */
    String getName();
}
