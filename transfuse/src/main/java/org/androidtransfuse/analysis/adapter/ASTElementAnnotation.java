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

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import com.google.inject.assistedinject.Assisted;

import javax.inject.Inject;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import java.util.Map;

/**
 * Element specific AST Annotation
 *
 * @author John Ericksen
 */
public class ASTElementAnnotation implements ASTAnnotation {

    private final AnnotationMirror annotationMirror;
    private final ASTType type;
    private final ElementConverterFactory elementConverterFactory;

    @Inject
    public ASTElementAnnotation(@Assisted AnnotationMirror annotationMirror,
                                @Assisted ASTType type,
                                ElementConverterFactory elementConverterFactory) {
        this.annotationMirror = annotationMirror;
        this.elementConverterFactory = elementConverterFactory;
        this.type = type;
    }

    @Override
    public ImmutableSet<String> getPropertyNames() {
        return FluentIterable.from(annotationMirror.getElementValues().keySet())
                .transform(new ExtractElementName())
                .toImmutableSet();
    }

    private static final class ExtractElementName implements Function<ExecutableElement, String> {
        @Override
        public String apply(ExecutableElement property) {
            return property.getSimpleName().toString();
        }
    }

    @Override
    public <T> T getProperty(String value, Class<T> type) {
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotationMirror.getElementValues().entrySet()) {
            if (value.equals(entry.getKey().getSimpleName().toString())) {
                return entry.getValue().accept(elementConverterFactory.buildAnnotationValueConverter(type), null);
            }
        }
        return null;
    }

    @Override
    public ASTType getASTType() {
        return type;
    }

    @Override
    public String toString() {
        return "ASTElementAnnotation{" +
                "type=" + type +
                '}';
    }
}
