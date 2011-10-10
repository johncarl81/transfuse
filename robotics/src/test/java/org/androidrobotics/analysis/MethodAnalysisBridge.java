package org.androidrobotics.analysis;

import javax.lang.model.element.ElementKind;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;

/**
 * @author John Ericksen
 */
public class MethodAnalysisBridge implements AnalysisBridge {

    private Method method;

    public MethodAnalysisBridge(Method method) {
        this.method = method;
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return method.getAnnotation(annotationClass);
    }

    @Override
    public String getName() {
        return method.getName();
    }

    @Override
    public Collection<AnalysisBridge> getEnclosedElements() {
        return Collections.emptySet();
    }

    @Override
    public ElementKind getType() {
        return ElementKind.METHOD;
    }
}
