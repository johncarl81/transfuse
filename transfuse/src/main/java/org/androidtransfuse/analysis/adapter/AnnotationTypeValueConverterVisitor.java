package org.androidtransfuse.analysis.adapter;

import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleAnnotationValueVisitor6;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Visitor to convert between the given annotation field and the given class type T
 *
 * @param <T>
 * @author John Ericksen
 */
public class AnnotationTypeValueConverterVisitor<T> extends SimpleAnnotationValueVisitor6<T, Void> {

    private final Class<T> type;
    private final ASTTypeBuilderVisitor astTypeBuilderVisitor;
    private final ElementConverterFactory astTypeElementConverterFactory;

    public AnnotationTypeValueConverterVisitor(Class<T> type,
                                               ASTTypeBuilderVisitor astTypeBuilderVisitor,
                                               ElementConverterFactory astTypeElementConverterFactory) {
        this.type = type;
        this.astTypeBuilderVisitor = astTypeBuilderVisitor;
        this.astTypeElementConverterFactory = astTypeElementConverterFactory;
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
        return variableElement.accept(astTypeElementConverterFactory.buildTypeConverter(type), null);
    }

    @Override
    public T visitArray(List<? extends AnnotationValue> annotationValues, Void aVoid) {
        List annotationASTTypes = new ArrayList();

        for (AnnotationValue annotationValue : annotationValues) {
            annotationASTTypes.add(annotationValue.accept(
                    new AnnotationTypeValueConverterVisitor(type.getComponentType(), astTypeBuilderVisitor, astTypeElementConverterFactory),
                    null));
        }

        return (T) annotationASTTypes.toArray((Object[]) Array.newInstance(type.getComponentType(), 0));
    }

    private <P> T visitSimple(Class<P> clazz, P value) {
        if (type.isAssignableFrom(clazz)) {
            return (T) value;
        }
        return null;
    }
}