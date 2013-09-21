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
package org.androidtransfuse.adapter;

import com.google.common.collect.ImmutableSet;

import java.lang.annotation.Annotation;

/**
 * Base class defining shared Abstract Syntax Tree elements
 *
 * @author John Ericksen
 */
public interface ASTBase {

    /**
     * Determines if the current element is annotated with the given annotation class
     *
     * @param annotation key
     * @return annotated conditional
     */
    boolean isAnnotated(Class<? extends Annotation> annotation);


    /**
     * Supplies the set of annotations
     *
     * @return ast annotation list
     */
    ImmutableSet<ASTAnnotation> getAnnotations();

    /**
     * Supplies the given annotation instance from the given annotation class key
     *
     * @param annotation key
     * @param <A>        annotation type
     * @return annotation instance
     */
    <A extends Annotation> A getAnnotation(Class<A> annotation);


    /**
     * Getter for the AST Annotation associated with the given annotation type
     *
     * @param annotation type
     * @return annotation AST
     */
    ASTAnnotation getASTAnnotation(Class<? extends Annotation> annotation);

    /**
     * Supplies the name of the current tree node
     *
     * @return name of the current node
     */
    String getName();
}
