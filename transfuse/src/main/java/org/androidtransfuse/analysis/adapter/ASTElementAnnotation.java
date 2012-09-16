package org.androidtransfuse.analysis.adapter;

import com.google.inject.assistedinject.Assisted;

import javax.inject.Inject;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import java.util.Map;

/**
 * Element specific AST Annotation
 *
 * @author John Ericksen
 */
public class ASTElementAnnotation implements ASTAnnotation {

    private final AnnotationMirror annotationMirror;
    private final ASTType type;
    private final ElementConverterFactory elementConverterFactory;

    @Inject
    public ASTElementAnnotation(@Assisted AnnotationMirror annotationMirror,
                                @Assisted ASTType type,
                                ElementConverterFactory elementConverterFactory) {
        this.annotationMirror = annotationMirror;
        this.elementConverterFactory = elementConverterFactory;
        this.type = type;
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
    public ASTType getASTType() {
        return type;
    }
}
