package org.androidtransfuse.analysis.adapter;

import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.apache.commons.lang.builder.EqualsBuilder;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;

/**
 * @author John Ericksen
 */
public class ASTTypeVirtualProxy implements ASTType {

    private final String name;
    private ASTType proxy;

    public ASTTypeVirtualProxy(String name) {
        this.name = name;
    }

    public void load(ASTType proxy){
        this.proxy = proxy;
    }

    private ASTType getProxy(){
        if(proxy != null){
            return proxy;
        }
        throw new TransfuseAnalysisException("Proxy not initialized prior to use");
    }

    @Override
    public Collection<ASTMethod> getMethods() {
        return getProxy().getMethods();
    }

    @Override
    public Collection<ASTField> getFields() {
        return getProxy().getFields();
    }

    @Override
    public Collection<ASTConstructor> getConstructors() {
        return getProxy().getConstructors();
    }

    @Override
    public boolean isConcreteClass() {
        return getProxy().isConcreteClass();
    }

    @Override
    public ASTType getSuperClass() {
        return getProxy().getSuperClass();
    }

    @Override
    public Collection<ASTType> getInterfaces() {
        return getProxy().getInterfaces();
    }

    @Override
    public boolean isArray() {
        return getProxy().isArray();
    }

    @Override
    public List<ASTType> getGenericParameters() {
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
    public Collection<ASTAnnotation> getAnnotations() {
        return getProxy().getAnnotations();
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotation) {
        return getProxy().getAnnotation(annotation);
    }

    @Override
    public ASTAnnotation getASTAnnotation(Class annotation) {
        return getProxy().getASTAnnotation(annotation);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (!(o instanceof ASTType)){
            return false;
        }

        ASTType that = (ASTType) o;

        return new EqualsBuilder().append(proxy, that).isEquals();
    }

    @Override
    public int hashCode() {
        return proxy != null ? proxy.hashCode() : 0;
    }
}
