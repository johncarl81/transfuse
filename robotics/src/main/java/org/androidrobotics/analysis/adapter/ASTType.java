package org.androidrobotics.analysis.adapter;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * @author John Ericksen
 */
public interface ASTType {

    <A extends Annotation> A getAnnotation(Class<A> annotation);

    Collection<ASTMethod> getMethods();

    Collection<ASTField> getFields();

    Collection<ASTConstructor> getConstructors();

    String getName();
}
