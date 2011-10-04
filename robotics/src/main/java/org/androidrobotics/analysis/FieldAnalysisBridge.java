package org.androidrobotics.analysis;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;

/**
 * @author John Ericksen
 */
public class FieldAnalysisBridge implements AnalysisBridge {

    private Field field;

    public FieldAnalysisBridge(Field field) {
        this.field = field;
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return field.getAnnotation(annotationClass);
    }

    @Override
    public String getName() {
        return field.getName();
    }

    @Override
    public Collection<AnalysisBridge> getEnclosedElements() {
        return Collections.emptySet();
    }
}
