package org.androidrobotics.analysis.adapter;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;

/**
 * @author John Ericksen
 */
public class ASTProxyType implements ASTType {

    private ASTType proxyASTType;
    private String name;

    public ASTProxyType(ASTType proxyASTType, String name) {
        this.proxyASTType = proxyASTType;
        this.name = name;
    }

    @Override
    public boolean isConcreteClass() {
        return true;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Collection<ASTMethod> getMethods() {
        return proxyASTType.getMethods();
    }

    @Override
    public Collection<ASTField> getFields() {
        return proxyASTType.getFields();
    }

    @Override
    public boolean isAnnotated(Class<? extends Annotation> annotation) {
        return proxyASTType.isAnnotated(annotation);
    }

    @Override
    public Collection<ASTAnnotation> getAnnotations() {
        return proxyASTType.getAnnotations();
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotation) {
        return proxyASTType.getAnnotation(annotation);
    }

    @Override
    public Collection<ASTConstructor> getConstructors() {
        return proxyASTType.getConstructors();
    }

    @Override
    public ASTType getSuperClass() {
        return null;
    }

    @Override
    public Collection<ASTType> getInterfaces() {
        return Collections.emptySet();
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ASTProxyType)) return false;

        ASTProxyType that = (ASTProxyType) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (proxyASTType != null ? !proxyASTType.equals(that.proxyASTType) : that.proxyASTType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = proxyASTType != null ? proxyASTType.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
