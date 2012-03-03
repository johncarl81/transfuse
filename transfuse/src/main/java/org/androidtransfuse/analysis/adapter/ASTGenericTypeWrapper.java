package org.androidtransfuse.analysis.adapter;

import com.google.inject.assistedinject.Assisted;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;

/**
 * @author John Ericksen
 */
public class ASTGenericTypeWrapper implements ASTType {

    private ASTType astType;
    private LazyTypeParameterBuilder lazyTypeParameterBuilder;

    @Inject
    public ASTGenericTypeWrapper(@Assisted ASTType astType, @Assisted LazyTypeParameterBuilder lazyTypeParameterBuilder) {
        this.astType = astType;
        this.lazyTypeParameterBuilder = lazyTypeParameterBuilder;
    }

    @Override
    public List<ASTType> getGenericParameters() {
        return lazyTypeParameterBuilder.buildGenericParameters();
    }

    public Collection<ASTMethod> getMethods() {
        return astType.getMethods();
    }

    public Collection<ASTField> getFields() {
        return astType.getFields();
    }

    public Collection<ASTConstructor> getConstructors() {
        return astType.getConstructors();
    }

    public boolean isConcreteClass() {
        return astType.isConcreteClass();
    }

    public ASTType getSuperClass() {
        return astType.getSuperClass();
    }

    public Collection<ASTType> getInterfaces() {
        return astType.getInterfaces();
    }

    public boolean isArray() {
        return astType.isArray();
    }

    public boolean isAnnotated(Class<? extends Annotation> annotation) {
        return astType.isAnnotated(annotation);
    }

    public Collection<ASTAnnotation> getAnnotations() {
        return astType.getAnnotations();
    }

    public <A extends Annotation> A getAnnotation(Class<A> annotation) {
        return astType.getAnnotation(annotation);
    }

    public String getName() {
        return astType.getName();
    }
}
