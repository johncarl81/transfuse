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
    private ASTTypeBuilderVisitor astTypeBuilderVisitor;

    public ASTElementAnnotation(AnnotationMirror annotationMirror, ASTTypeBuilderVisitor astTypeBuilderVisitor) {
        this.annotationMirror = annotationMirror;
        this.astTypeBuilderVisitor = astTypeBuilderVisitor;
    }

    @Override
    public <T> T getProperty(String value, Class<T> type) {
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotationMirror.getElementValues().entrySet()) {
            if (value.equals(entry.getKey().getSimpleName().toString())) {
                return entry.getValue().accept(new AnnotationTypeValueConverterVisitor<T>(type, astTypeBuilderVisitor), null);
            }
        }
        return null;
    }

    @Override
    public String getName() {
        return annotationMirror.getAnnotationType().asElement().toString();
    }
}
