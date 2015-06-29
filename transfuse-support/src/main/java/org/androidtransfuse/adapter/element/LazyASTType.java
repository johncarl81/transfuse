package org.androidtransfuse.adapter.element;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.androidtransfuse.adapter.*;
import org.apache.commons.lang.builder.EqualsBuilder;

import java.lang.annotation.Annotation;

/**
 * @author John Ericksen
 */
public abstract class LazyASTType implements ASTType{

    private final PackageClass packageClass;
    private ASTType proxy;

    public LazyASTType(PackageClass packageClass) {
        this.packageClass = packageClass;
    }

    private synchronized ASTType getProxy() {
        if (proxy == null) {
            proxy = lazyLoad();
        }
        return proxy;
    }

    public abstract ASTType lazyLoad();

    @Override
    public ImmutableSet<ASTMethod> getMethods() {
        return getProxy().getMethods();
    }

    @Override
    public boolean isFinal() {
        return getProxy().isFinal();
    }

    @Override
    public ImmutableSet<ASTField> getFields() {
        return getProxy().getFields();
    }

    @Override
    public ImmutableSet<ASTConstructor> getConstructors() {
        return getProxy().getConstructors();
    }

    @Override
    public boolean isConcreteClass() {
        return getProxy().isConcreteClass();
    }

    @Override
    public boolean isInterface() {
        return getProxy().isInterface();
    }

    @Override
    public ASTType getSuperClass() {
        return getProxy().getSuperClass();
    }

    @Override
    public ImmutableSet<ASTType> getInterfaces() {
        return getProxy().getInterfaces();
    }

    @Override
    public ImmutableList<ASTType> getGenericParameters() {
        return getProxy().getGenericParameters();
    }

    @Override
    public boolean inheritsFrom(ASTType type) {
        return getProxy().inheritsFrom(type);
    }

    @Override
    public boolean extendsFrom(ASTType type) {
        return getProxy().extendsFrom(type);
    }

    @Override
    public boolean implementsFrom(ASTType type) {
        return getProxy().implementsFrom(type);
    }

    @Override
    public boolean isAnnotated(Class<? extends Annotation> annotation) {
        return getProxy().isAnnotated(annotation);
    }

    @Override
    public ImmutableSet<ASTAnnotation> getAnnotations() {
        return getProxy().getAnnotations();
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotation) {
        return getProxy().getAnnotation(annotation);
    }

    @Override
    public ASTAnnotation getASTAnnotation(Class<? extends Annotation> annotation) {
        return getProxy().getASTAnnotation(annotation);
    }

    @Override
    public String getName() {
        return packageClass.getCanonicalName();
    }

    @Override
    public PackageClass getPackageClass() {
        return packageClass;
    }

    @Override
    public String toString() {
        return getProxy().toString();
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

        return new EqualsBuilder().append(getProxy(), that).isEquals();
    }

    @Override
    public int hashCode() {
        return getProxy().hashCode();
    }
}
