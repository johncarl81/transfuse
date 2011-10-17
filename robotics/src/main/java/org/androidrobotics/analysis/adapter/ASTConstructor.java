package org.androidrobotics.analysis.adapter;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @author John Ericksen
 */
public interface ASTConstructor {

    boolean isAnnotated(Class<? extends Annotation> annotation);

    List<ASTParameter> getParameters();

    String getName();
}
