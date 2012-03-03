package org.androidtransfuse.analysis.adapter;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Class specific AST Type
 *
 * @author John Ericksen
 */
public class ASTClassType implements ASTType {

    private Class<?> clazz;
    private Collection<ASTAnnotation> annotationList;
    private Collection<ASTMethod> methods;
    private Collection<ASTConstructor> constructors;
    private Collection<ASTField> fields;
    private ASTType superClass;
    private Collection<ASTType> interfaces;

    public ASTClassType(Class<?> clazz, Collection<ASTAnnotation> annotationList, Collection<ASTConstructor> constructors, Collection<ASTMethod> methods, Collection<ASTField> fields, ASTType superClass, Collection<ASTType> interfaces) {
        this.clazz = clazz;
        this.annotationList = annotationList;
        this.constructors = constructors;
        this.methods = methods;
        this.fields = fields;
        this.superClass = superClass;
        this.interfaces = interfaces;
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotation) {
        return clazz.getAnnotation(annotation);
    }

    @Override
    public boolean isAnnotated(Class<? extends Annotation> annotation) {
        return clazz.isAnnotationPresent(annotation);
    }

    @Override
    public Collection<ASTMethod> getMethods() {
        return methods;
    }

    @Override
    public Collection<ASTField> getFields() {
        return fields;
    }

    @Override
    public Collection<ASTConstructor> getConstructors() {
        return constructors;
    }

    @Override
    public String getName() {
        return clazz.getName();
    }

    @Override
    public boolean isConcreteClass() {
        return !clazz.isInterface() && !clazz.isSynthetic();
    }

    @Override
    public Collection<ASTAnnotation> getAnnotations() {
        return annotationList;
    }

    @Override
    public ASTType getSuperClass() {
        return superClass;
    }

    @Override
    public Collection<ASTType> getInterfaces() {
        return interfaces;
    }

    @Override
    public boolean isArray() {
        return clazz.isArray();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ASTClassType)) {
            return false;
        }

        ASTClassType that = (ASTClassType) o;

        if (clazz != null ? !clazz.equals(that.clazz) : that.clazz != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return clazz != null ? clazz.hashCode() : 0;
    }

    @Override
    public List<ASTType> getGenericParameters() {
        return Collections.emptyList();
    }
}
