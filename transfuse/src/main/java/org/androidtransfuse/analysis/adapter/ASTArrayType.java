package org.androidtransfuse.analysis.adapter;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Specific AST Array Type.
 * <p/>
 * This ASTType wraps a delegate ASTType, decorating it with array attributes.
 *
 * @author John Ericksen
 */
public class ASTArrayType implements ASTType {

    private ASTType delegate;

    public ASTArrayType(ASTType delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public boolean isAnnotated(Class<? extends Annotation> annotation) {
        return delegate.isAnnotated(annotation);
    }

    @Override
    public Collection<ASTAnnotation> getAnnotations() {
        return delegate.getAnnotations();
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotation) {
        return delegate.getAnnotation(annotation);
    }

    @Override
    public String getName() {
        return delegate.getName() + "[]";
    }

    @Override
    public Collection<ASTMethod> getMethods() {
        return delegate.getMethods();
    }

    @Override
    public Collection<ASTField> getFields() {
        return delegate.getFields();
    }

    @Override
    public Collection<ASTConstructor> getConstructors() {
        return delegate.getConstructors();
    }

    @Override
    public boolean isConcreteClass() {
        return delegate.isConcreteClass();
    }

    @Override
    public ASTType getSuperClass() {
        return delegate.getSuperClass();
    }

    @Override
    public Collection<ASTType> getInterfaces() {
        return delegate.getInterfaces();
    }

    @Override
    public List<ASTType> getGenericParameters() {
        return Collections.emptyList();
    }

    @Override
    public boolean inheritsFrom(ASTType type) {
        return delegate.inheritsFrom(type);
    }

    @Override
    public boolean extendsFrom(ASTType type) {
        return delegate.extendsFrom(type);
    }

    @Override
    public boolean implementsFrom(ASTType type) {
        return delegate.implementsFrom(type);
    }

    @Override
    public ASTAnnotation getASTAnnotation(Class annotation) {
        return delegate.getASTAnnotation(annotation);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ASTArrayType)) {
            return false;
        }

        ASTArrayType that = (ASTArrayType) o;

        return new EqualsBuilder().append(isArray(), that.isArray()).append(delegate, that.delegate).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(delegate).hashCode();
    }
}
