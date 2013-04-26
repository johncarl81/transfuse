/**
 * Copyright 2013 John Ericksen
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

import org.androidtransfuse.adapter.*;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;

/**
 * Builds the appropriate (expected) ASTType for the visited Element
 *
 * @param <T>
 */
public class ASTTypeElementConverter<T> extends ElementVisitorAdaptor<T, Void> {

    private static final String CONSTRUCTOR_IDENTIFIER = "<init>";
    private static final String STATIC_INITIALIZER_IDENTIFIER = "<clinit>";

    private final Class<T> astTypeClass;
    private final ASTElementFactory astElementFactory;

    public ASTTypeElementConverter(Class<T> astTypeClass,
                                   ASTElementFactory astElementFactory) {
        this.astTypeClass = astTypeClass;
        this.astElementFactory = astElementFactory;
    }

    @Override
    public T visitType(TypeElement typeElement, Void aVoid) {
        if (astTypeClass.isAssignableFrom(ASTType.class)) {
            return (T) astElementFactory.getType(typeElement);
        }
        return null;
    }

    @Override
    public T visitVariable(VariableElement variableElement, Void aVoid) {
        if (astTypeClass.isAssignableFrom(ASTField.class)) {
            return (T) astElementFactory.getField(variableElement);
        }
        return null;
    }

    @Override
    public T visitExecutable(ExecutableElement executableElement, Void aVoid) {
        //constructors and methods share this Element, the indication that the method is a constructor
        //is that it is named <init>
        if (executableElement.getSimpleName().contentEquals(STATIC_INITIALIZER_IDENTIFIER)){
            return null; // ignoring static initializer block
        }
        if (executableElement.getSimpleName().contentEquals(CONSTRUCTOR_IDENTIFIER)) {
            if (astTypeClass.isAssignableFrom(ASTConstructor.class)) {
                return (T) astElementFactory.getConstructor(executableElement);
            }
        } else if (astTypeClass.isAssignableFrom(ASTMethod.class)) {
            return (T) astElementFactory.getMethod(executableElement);
        }
        return null;
    }

    @Override
    public T visitTypeParameter(TypeParameterElement typeParameterElement, Void aVoid) {
        if (astTypeClass.isAssignableFrom(ASTParameter.class)) {
            return (T) astElementFactory.getParameter(typeParameterElement);
        }
        return null;
    }
}