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
package org.androidtransfuse.analysis.adapter;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Factory to create Element Converters
 *
 * @author John Ericksen
 */
public class ElementConverterFactory {

    private final ASTTypeBuilderVisitor astTypeBuilderVisitor;
    private final Provider<ASTElementFactory> astElementFactoryProvider;
    private final ASTFactory astFactory;

    @Inject
    public ElementConverterFactory(ASTTypeBuilderVisitor astTypeBuilderVisitor,
                                   Provider<ASTElementFactory> astElementFactoryProvider,
                                   ASTFactory astFactory) {
        this.astTypeBuilderVisitor = astTypeBuilderVisitor;
        this.astElementFactoryProvider = astElementFactoryProvider;
        this.astFactory = astFactory;
    }

    public <T> ASTTypeElementConverter<T> buildTypeConverter(Class<T> clazz) {
        return new ASTTypeElementConverter<T>(clazz, astElementFactoryProvider.get());
    }

    public <T> AnnotationTypeValueConverterVisitor<T> buildAnnotationValueConverter(Class<T> clazz) {
        return new AnnotationTypeValueConverterVisitor<T>(clazz, astTypeBuilderVisitor, this, astFactory);
    }
}
