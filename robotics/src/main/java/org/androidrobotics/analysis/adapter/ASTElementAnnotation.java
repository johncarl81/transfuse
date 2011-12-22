package org.androidrobotics.analysis.adapter;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class ASTElementAnnotation implements ASTAnnotation {

    private AnnotationMirror annotationMirror;

    public ASTElementAnnotation(AnnotationMirror annotationMirror) {
        this.annotationMirror = annotationMirror;
    }

    @Override
    public Object getProperty(String value) {
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotationMirror.getElementValues().entrySet()) {
            if (value.equals(entry.getKey().getSimpleName().toString())) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public String getName() {
        return annotationMirror.getAnnotationType().asElement().toString();
    }
}
