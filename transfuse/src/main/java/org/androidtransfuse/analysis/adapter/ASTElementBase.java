package org.androidtransfuse.analysis.adapter;

import com.google.common.collect.ImmutableCollection;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * Element specific ASTBase implementation
 *
 * @author John Ericksen
 */
public class ASTElementBase implements ASTBase {

    private final Element element;
    private final ImmutableCollection<ASTAnnotation> annotations;

    public ASTElementBase(Element element, ImmutableCollection<ASTAnnotation> annotations) {
        this.element = element;
        this.annotations = annotations;
    }

    public boolean isAnnotated(Class<? extends Annotation> annotation) {
        return getASTAnnotation(annotation) != null;
    }

    public <A extends Annotation> A getAnnotation(Class<A> annotation) {
        return element.getAnnotation(annotation);
    }

    public String getName() {
        return element.getSimpleName().toString();
    }

    @Override
    public Collection<ASTAnnotation> getAnnotations() {
        return annotations;
    }

    @Override
    public ASTAnnotation getASTAnnotation(Class annotation) {
        return ASTUtils.getInstance().getAnnotation(annotation, getAnnotations());
    }
}
