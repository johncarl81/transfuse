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

import com.google.common.base.Function;

import javax.lang.model.element.Element;

/**
 * Element to AST converter, converts the given type of javax.lang.model.element.Element to the
 * transfuse internal AST representation.
 *
 * @author John Ericksen
 */
public class ASTElementConverter<T> implements Function<Element, T> {

    private final Class<T> astTypeClass;
    private final ElementConverterFactory elementConverterFactory;

    public ASTElementConverter(Class<T> astTypeClass, ElementConverterFactory elementConverterFactory) {
        this.astTypeClass = astTypeClass;
        this.elementConverterFactory = elementConverterFactory;
    }

    @Override
    public T apply( Element element) {
        //visit the given element to determine its type, feed it into the appropriate
        //ASTElementFactory method and return the result
        return element.accept(elementConverterFactory.buildTypeConverter(astTypeClass), null);
    }
}
