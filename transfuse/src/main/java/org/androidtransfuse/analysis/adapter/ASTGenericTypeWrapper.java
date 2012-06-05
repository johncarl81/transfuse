package org.androidtransfuse.analysis.adapter;

import com.google.inject.assistedinject.Assisted;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

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

    @Override
    public boolean inheritsFrom(ASTType type) {
        return astType.inheritsFrom(type);
    }

    @Override
    public boolean extendsFrom(ASTType type) {
        return astType.extendsFrom(type);
    }

    @Override
    public boolean implementsFrom(ASTType type) {
        return astType.implementsFrom(type);
    }

    @Override
    public ASTAnnotation getASTAnnotation(Class annotation) {
        return astType.getASTAnnotation(annotation);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ASTType)) {
            return false;
        }

        ASTType that = (ASTType) o;

        return new EqualsBuilder().append(getName(), that.getName()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getName()).hashCode();
    }
}
