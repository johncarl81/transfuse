package org.androidrobotics.analysis.adapter;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author John Ericksen
 */
public class ASTClassType implements ASTType {

    private Class<?> clazz;
    private Collection<ASTMethod> methods;
    private Collection<ASTConstructor> constructors;
    private Collection<ASTField> fields;

    public ASTClassType(Class<?> clazz, Collection<ASTConstructor> constructors, Collection<ASTMethod> methods, Collection<ASTField> fields) {
        this.clazz = clazz;
        this.constructors = constructors;
        this.methods = methods;
        this.fields = fields;
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
    public List<ASTAnnotation> getAnnotations() {
        List<ASTAnnotation> annotationList = new ArrayList<ASTAnnotation>();

        for (Annotation annotation : clazz.getAnnotations()) {
            annotationList.add(new ASTClassAnnotation(annotation));
        }

        return annotationList;
    }
}
