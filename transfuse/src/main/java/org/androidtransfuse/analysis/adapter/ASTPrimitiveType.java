package org.androidtransfuse.analysis.adapter;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Element specific primitive AST type
 *
 * @author John Ericksen
 */
public enum ASTPrimitiveType implements ASTType {

    BOOLEAN("boolean", Boolean.class),
    BYTE("byte", Byte.class),
    SHORT("short", Short.class),
    CHAR("char", Character.class),
    INT("int", Integer.class),
    FLOAT("float", Float.class),
    LONG("long", Long.class),
    DOUBLE("double", Double.class);

    private Class clazz;
    private String label;

    private ASTPrimitiveType(String label, Class clazz) {
        this.label = label;
        this.clazz = clazz;
    }

    @Override
    public boolean isAnnotated(Class<? extends Annotation> annotation) {
        return false;
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotation) {
        return null;
    }

    @Override
    public Collection<ASTMethod> getMethods() {
        return Collections.emptySet();
    }

    @Override
    public Collection<ASTField> getFields() {
        return Collections.emptySet();
    }

    @Override
    public Collection<ASTConstructor> getConstructors() {
        return Collections.emptySet();
    }

    @Override
    public String getName() {
        return label;
    }

    public Class getObjectClass() {
        return clazz;
    }

    @Override
    public boolean isConcreteClass() {
        return false;
    }

    @Override
    public Collection<ASTAnnotation> getAnnotations() {
        return Collections.emptyList();
    }

    @Override
    public Collection<ASTType> getInterfaces() {
        return Collections.emptySet();
    }

    @Override
    public ASTType getSuperClass() {
        return null;
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public List<ASTType> getGenericParameters() {
        return Collections.emptyList();
    }
}
