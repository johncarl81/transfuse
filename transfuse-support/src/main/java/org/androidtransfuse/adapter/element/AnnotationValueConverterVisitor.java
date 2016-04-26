/**
 * Copyright 2011-2015 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidtransfuse.adapter.element;

import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTFactory;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.transaction.TransactionRuntimeException;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ElementKind;
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
public class AnnotationValueConverterVisitor<T> extends SimpleAnnotationValueVisitor6<T, Void> {

    private static final String ERROR_TYPE = "<error>";

    private final Class<T> type;
    private final ASTTypeBuilderVisitor astTypeBuilderVisitor;
    private final ElementConverterFactory astTypeElementConverterFactory;
    private final ASTFactory astFactory;

    public AnnotationValueConverterVisitor(Class<T> type,
                                           ASTTypeBuilderVisitor astTypeBuilderVisitor,
                                           ElementConverterFactory astTypeElementConverterFactory,
                                           ASTFactory astFactory) {
        this.type = type;
        this.astTypeBuilderVisitor = astTypeBuilderVisitor;
        this.astTypeElementConverterFactory = astTypeElementConverterFactory;
        this.astFactory = astFactory;
    }

    @Override
    public T visitBoolean(boolean b, Void aVoid) {
        return visitPrimitive(boolean.class, Boolean.class, b);
    }

    @Override
    public T visitByte(byte b, Void aVoid) {
        return visitPrimitive(byte.class, Byte.class, b);
    }

    @Override
    public T visitChar(char c, Void aVoid) {
        return visitPrimitive(char.class, Character.class, c);
    }

    @Override
    public T visitDouble(double d, Void aVoid) {
        return visitPrimitive(double.class, Double.class, d);
    }

    @Override
    public T visitFloat(float f, Void aVoid) {
        return visitPrimitive(float.class, Float.class, f);
    }

    @Override
    public T visitInt(int i, Void aVoid) {
        return visitPrimitive(int.class, Integer.class, i);
    }

    @Override
    public T visitLong(long l, Void aVoid) {
        return visitPrimitive(long.class, Long.class, l);
    }

    @Override
    public T visitShort(short s, Void aVoid) {
        return visitPrimitive(short.class, Short.class, s);
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
        if(variableElement.getKind() == ElementKind.ENUM_CONSTANT){
            return (T) Enum.valueOf((Class<? extends Enum>) type, variableElement.getSimpleName().toString());
        }
        return null;
    }

    @Override
    public T visitArray(List<? extends AnnotationValue> annotationValues, Void aVoid) {
        List annotationASTTypes = new ArrayList();

        for (AnnotationValue annotationValue : annotationValues) {
            annotationASTTypes.add(annotationValue.accept(
                    new AnnotationValueConverterVisitor(type.getComponentType(), astTypeBuilderVisitor, astTypeElementConverterFactory, astFactory),
                    null));
        }

        return (T) annotationASTTypes.toArray((Object[]) Array.newInstance(type.getComponentType(), 0));
    }

    private <P> T visitPrimitive(Class<P> primitiveClazz, Class<P> boxedClass, P value){
        if (type.isAssignableFrom(primitiveClazz)){
            return (T) value;
        }

        return visitSimple(boxedClass, value);
    }

    private <P> T visitSimple(Class<P> clazz, P value) {
        // The ErrorType is represented in annotations as a string value "<error>".  Therefore, we need to watch for
        // this and throw an Exception if the case arises.
        if (value.equals(ERROR_TYPE)){
            throw new TransactionRuntimeException("Encountered ErrorType " + value.toString() + ", unable to recover");
        }
        if (type.isAssignableFrom(clazz)) {
            return (T) value;
        }
        return null;
    }

    @Override
    public T visitAnnotation(AnnotationMirror annotationMirror, Void aVoid) {
        if (type.isAssignableFrom(ASTAnnotation.class)){
            ASTType annotationType = annotationMirror.getAnnotationType().accept(astTypeBuilderVisitor, null);
            return (T) astFactory.buildASTElementAnnotation(annotationMirror, annotationType);
        }

        return null;
    }
}