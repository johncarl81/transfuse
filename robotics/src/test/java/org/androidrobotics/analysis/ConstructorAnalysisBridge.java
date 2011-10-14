package org.androidrobotics.analysis;

import javax.lang.model.element.ElementKind;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Collections;

/**
 * @author John Ericksen
 */
public class ConstructorAnalysisBridge implements AnalysisBridge {

    private Constructor constructor;

    public ConstructorAnalysisBridge(Constructor constructor) {
        this.constructor = constructor;
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return (T) constructor.getAnnotation(annotationClass);
    }

    @Override
    public String getName() {
        return constructor.getName();
    }

    @Override
    public Collection<AnalysisBridge> getEnclosedElements() {
        return Collections.emptySet();
    }

    @Override
    public ElementKind getType() {
        return ElementKind.CONSTRUCTOR;
    }

    @Override
    public <A extends Annotation> boolean isAnnotated(Class<A> annotationClass) {
        return getAnnotation(annotationClass) != null;
    }
}
