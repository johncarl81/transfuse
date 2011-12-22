package org.androidrobotics.analysis.adapter;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Element specific ASTBase implementation
 *
 * @author John Ericksen
 */
public class ASTElementBase implements ASTBase {

    private Element element;

    public ASTElementBase(Element element) {
        this.element = element;
    }

    public boolean isAnnotated(Class<? extends Annotation> annotation) {
        return getAnnotation(annotation) != null;
    }

    public <A extends Annotation> A getAnnotation(Class<A> annotation) {
        return element.getAnnotation(annotation);
    }

    public String getName() {
        return element.getSimpleName().toString();
    }

    @Override
    public List<ASTAnnotation> getAnnotations() {

        List<ASTAnnotation> annotations = new ArrayList<ASTAnnotation>();

        for (AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
            annotations.add(new ASTElementAnnotation(annotationMirror));
        }

        return annotations;
    }
}
