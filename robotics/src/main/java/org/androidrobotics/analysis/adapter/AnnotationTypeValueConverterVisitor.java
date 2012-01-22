package org.androidrobotics.analysis.adapter;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.AnnotationValueVisitor;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;

public class AnnotationTypeValueConverterVisitor<T> implements AnnotationValueVisitor<T, Void> {

    private Class type;
    private ASTTypeBuilderVisitor astTypeBuilderVisitor;

    public AnnotationTypeValueConverterVisitor(Class type, ASTTypeBuilderVisitor astTypeBuilderVisitor) {
        this.type = type;
        this.astTypeBuilderVisitor = astTypeBuilderVisitor;
    }

    @Override
    public T visit(AnnotationValue annotationValue, Void aVoid) {
        return null;
    }

    @Override
    public T visit(AnnotationValue annotationValue) {
        return null;
    }

    @Override
    public T visitBoolean(boolean b, Void aVoid) {
        return visitSimple(Boolean.class, b);
    }

    @Override
    public T visitByte(byte b, Void aVoid) {
        return visitSimple(Byte.class, b);
    }

    @Override
    public T visitChar(char c, Void aVoid) {
        return visitSimple(Character.class, c);
    }

    @Override
    public T visitDouble(double d, Void aVoid) {
        return visitSimple(Double.class, d);
    }

    @Override
    public T visitFloat(float f, Void aVoid) {
        return visitSimple(Float.class, f);
    }

    @Override
    public T visitInt(int i, Void aVoid) {
        return visitSimple(Integer.class, i);
    }

    @Override
    public T visitLong(long l, Void aVoid) {
        return visitSimple(Long.class, l);
    }

    @Override
    public T visitShort(short s, Void aVoid) {
        return visitSimple(Short.class, s);
    }

    @Override
    public T visitString(String s, Void aVoid) {
        return visitSimple(String.class, s);
    }

    @Override
    public T visitType(TypeMirror typeMirror, Void aVoid) {
        if (type.isAssignableFrom(ASTType.class)) {
            return (T) typeMirror.accept(astTypeBuilderVisitor, null);
        }
        return null;
    }

    @Override
    public T visitEnumConstant(VariableElement variableElement, Void aVoid) {
        return null;//todo:finish
    }

    @Override
    public T visitAnnotation(AnnotationMirror annotationMirror, Void aVoid) {
        return null;
    }

    @Override
    public T visitArray(List<? extends AnnotationValue> annotationValues, Void aVoid) {
        return null;
    }

    @Override
    public T visitUnknown(AnnotationValue annotationValue, Void aVoid) {
        return null;
    }

    private <P> T visitSimple(Class<P> clazz, P value) {
        if (type.isAssignableFrom(clazz)) {
            return (T) value;
        }
        return null;
    }
}