package org.androidtransfuse.analysis.adapter;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Replaces the given AST Type with a proxy type.  This is used during AOP and Virtual proxy of the given
 * ASTType.  Simply replaces the name of the class with the proxy name.
 *
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
        //proxy name
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
        return proxyASTType.isArray();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ASTProxyType)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        ASTProxyType rhs = (ASTProxyType) obj;
        return new EqualsBuilder()
                .append(name, rhs.name)
                .append(proxyASTType, rhs.proxyASTType)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).append(proxyASTType).hashCode();
    }

    @Override
    public List<ASTType> getGenericParameters() {
        return proxyASTType.getGenericParameters();
    }

    @Override
    public boolean inheritsFrom(ASTType type) {
        return proxyASTType.inheritsFrom(type);
    }

    @Override
    public boolean extendsFrom(ASTType type) {
        return proxyASTType.extendsFrom(type);
    }

    @Override
    public boolean implementsFrom(ASTType type) {
        return proxyASTType.implementsFrom(type);
    }

    @Override
    public ASTAnnotation getASTAnnotation(Class annotation) {
        return proxyASTType.getASTAnnotation(annotation);
    }

    @Override
    public String toString(){
        return getName();
    }
}
