package org.androidtransfuse.analysis.adapter;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * Class specific AST Parameter
 *
 * @author John Ericksen
 */
public class ASTClassParameter implements ASTParameter {

    private final ASTType astType;
    private final ImmutableMap<Class<?>, Annotation> annotationMap;
    private final ImmutableCollection<ASTAnnotation> annotations;

    public ASTClassParameter(Annotation[] annotations, ASTType astType, ImmutableCollection<ASTAnnotation> astAnnotations) {
        this.annotations = astAnnotations;
        this.astType = astType;

        ImmutableMap.Builder<Class<?>, Annotation> classAnnotationBuilder = ImmutableMap.builder();
        for (Annotation annotation : annotations) {
            classAnnotationBuilder.put(annotation.getClass(), annotation);
        }

        this.annotationMap = classAnnotationBuilder.build();
    }

    public boolean isAnnotated(Class<? extends Annotation> annotation) {
        return annotationMap.containsKey(annotation);
    }

    public <A extends Annotation> A getAnnotation(Class<A> annotation) {
        return (A) annotationMap.get(annotation);
    }

    @Override
    public String getName() {
        return astType.getName();
    }

    @Override
    public ASTType getASTType() {
        return astType;
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
