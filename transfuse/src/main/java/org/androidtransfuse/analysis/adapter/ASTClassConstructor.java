package org.androidtransfuse.analysis.adapter;

import com.google.common.collect.ImmutableList;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.List;

/**
 * Class specific AST Constructor
 *
 * @author John Ericksen
 */
public class ASTClassConstructor implements ASTConstructor {

    private final Constructor constructor;
    private final ImmutableList<ASTParameter> parameters;
    private final ASTAccessModifier modifier;
    private final ImmutableList<ASTAnnotation> annotations;
    private final ImmutableList<ASTType> throwsTypes;

    public ASTClassConstructor(ImmutableList<ASTAnnotation> annotations, Constructor<?> constructor, ImmutableList<ASTParameter> parameters, ASTAccessModifier modifier, ImmutableList<ASTType> throwsTypes) {
        this.annotations = annotations;
        this.constructor = constructor;
        this.parameters = parameters;
        this.modifier = modifier;
        this.throwsTypes = throwsTypes;
    }

    @Override
    public boolean isAnnotated(Class<? extends Annotation> annotation) {
        return constructor.isAnnotationPresent(annotation);
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotation) {
        return (A) constructor.getAnnotation(annotation);
    }

    @Override
    public List<ASTParameter> getParameters() {
        return parameters;
    }

    @Override
    public String getName() {
        return constructor.getName();
    }

    @Override
    public Collection<ASTAnnotation> getAnnotations() {
        return annotations;
    }

    @Override
    public ASTAccessModifier getAccessModifier() {
        return modifier;
    }

    @Override
    public List<ASTType> getThrowsTypes() {
        return throwsTypes;
    }

    @Override
    public ASTAnnotation getASTAnnotation(Class annotation) {
        return ASTUtils.getInstance().getAnnotation(annotation, getAnnotations());
    }
}
