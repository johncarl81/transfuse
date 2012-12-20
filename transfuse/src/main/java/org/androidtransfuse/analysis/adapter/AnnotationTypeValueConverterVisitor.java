/**
 * Copyright 2012 John Ericksen
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
package org.androidtransfuse.analysis.adapter;

import org.androidtransfuse.processor.TransactionRuntimeException;

import javax.lang.model.element.AnnotationMirror;
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

    private static final String ERROR_TYPE = "<error>";

    private final Class<T> type;
    private final ASTTypeBuilderVisitor astTypeBuilderVisitor;
    private final ElementConverterFactory astTypeElementConverterFactory;
    private final ASTFactory astFactory;

    public AnnotationTypeValueConverterVisitor(Class<T> type,
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
                    new AnnotationTypeValueConverterVisitor(type.getComponentType(), astTypeBuilderVisitor, astTypeElementConverterFactory, astFactory),
                    null));
        }

        return (T) annotationASTTypes.toArray((Object[]) Array.newInstance(type.getComponentType(), 0));
    }

    private <P> T visitSimple(Class<P> clazz, P value) {
        if (type.isAssignableFrom(clazz)) {
            return (T) value;
        }
        // The ErrorType is represented in annotations as a string value "<error>".  Therefore, we need to watch for
        // this and throw an Exception if the case arises.
        if (type.equals(ASTType.class) && value.equals(ERROR_TYPE)){
            throw new TransactionRuntimeException("Encountered ErrorType " + value.toString() + ", unable to recover");
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