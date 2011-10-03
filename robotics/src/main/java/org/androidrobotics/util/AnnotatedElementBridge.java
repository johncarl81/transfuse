package org.androidrobotics.util;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * @author John Ericksen
 */
public class AnnotatedElementBridge implements AnnotatedElement {

    private Element element;

    public AnnotatedElementBridge(Element element) {
        this.element = element;
    }

    @Override
    public boolean isAnnotationPresent(Class<? extends Annotation> aClass) {
        return element.getAnnotation(aClass) != null;
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> tClass) {
        return element.getAnnotation(tClass);
    }

    @Override
    public Annotation[] getAnnotations() {
        return new Annotation[0];
    }

    @Override
    public Annotation[] getDeclaredAnnotations() {
        return new Annotation[0];
    }
}
