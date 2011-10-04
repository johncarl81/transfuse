package org.androidrobotics.analysis;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * @author John Ericksen
 */
public interface AnalysisBridge {

    <T extends Annotation> T getAnnotation(Class<T> annotationClass);

    String getName();

    Collection<AnalysisBridge> getEnclosedElements();
}
