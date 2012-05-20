package org.androidtransfuse.analysis.adapter;

import java.lang.annotation.Annotation;
import java.util.*;

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

    private static final Map<String, ASTPrimitiveType> AUTOBOX_TYPE_MAP = new HashMap<String, ASTPrimitiveType>();

    private Class clazz;
    private String label;

    static {
        for (ASTPrimitiveType astPrimitive : ASTPrimitiveType.values()) {
            AUTOBOX_TYPE_MAP.put(astPrimitive.getObjectClass().getName(), astPrimitive);
        }
    }

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

    public static ASTPrimitiveType getAutoboxType(String name) {
        return AUTOBOX_TYPE_MAP.get(name);
    }

    @Override
    public boolean inheritsFrom(ASTType type) {
        return false;
    }

    @Override
    public boolean extendsFrom(ASTType type) {
        return false;
    }

    @Override
    public boolean implementsFrom(ASTType type) {
        return false;
    }

    @Override
    public ASTAnnotation getASTAnnotation(Class annotation) {
        return ASTUtils.getInstance().getAnnotation(annotation, getAnnotations());
    }
}
