package org.androidrobotics.analysis;

import java.lang.annotation.Annotation;

/**
 * @author John Ericksen
 */
public abstract class ReflectionAnalysisBridgeBase implements AnalysisBridge {

    @Override
    public <A extends Annotation> boolean isAnnotated(Class<A> annotationClass) {
        return getAnnotation(annotationClass) != null;
    }
}
