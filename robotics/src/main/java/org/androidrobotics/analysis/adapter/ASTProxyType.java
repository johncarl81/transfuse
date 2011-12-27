package org.androidrobotics.analysis.adapter;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;

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
    public List<ASTAnnotation> getAnnotations() {
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
}
