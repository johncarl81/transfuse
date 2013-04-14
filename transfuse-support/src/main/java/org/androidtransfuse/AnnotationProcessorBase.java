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
package org.androidtransfuse;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

import javax.annotation.processing.AbstractProcessor;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

/**
 * @author John Ericksen
 */
public abstract class AnnotationProcessorBase extends AbstractProcessor {

    /**
     * Gets the supported annotations from the @SupportedAnnotations annotation, which deals with classes instead of
     * strings.
     *
     * @return Set of supported annotation names
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Class<? extends Annotation>[] supportedAnnotations = getClass().getAnnotation(SupportedAnnotations.class).value();

        return FluentIterable
                .from(Arrays.asList(supportedAnnotations))
                .transform(new ClassToNameTransform())
                .toSet();
    }

    private static class ClassToNameTransform implements Function<Class, String> {
        @Override
        public String apply(Class input) {
            return input.getName();
        }
    }
}
