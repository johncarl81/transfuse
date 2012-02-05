package org.androidtransfuse.analysis.adapter;

import com.google.inject.assistedinject.Assisted;

import javax.inject.Inject;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class ASTElementAnnotation implements ASTAnnotation {

    private AnnotationMirror annotationMirror;
    private ElementConverterFactory elementConverterFactory;

    @Inject
    public ASTElementAnnotation(@Assisted AnnotationMirror annotationMirror,
                                ElementConverterFactory elementConverterFactory) {
        this.annotationMirror = annotationMirror;
        this.elementConverterFactory = elementConverterFactory;
    }

    @Override
    public <T> T getProperty(String value, Class<T> type) {
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotationMirror.getElementValues().entrySet()) {
            if (value.equals(entry.getKey().getSimpleName().toString())) {
                return entry.getValue().accept(elementConverterFactory.buildAnnotationValueConverter(type), null);
            }
        }
        return null;
    }

    @Override
    public String getName() {
        return annotationMirror.getAnnotationType().asElement().toString();
    }
}
