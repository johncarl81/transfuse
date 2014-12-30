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
package org.androidtransfuse.adapter;

import com.google.common.collect.ImmutableSet;

/**
 * Abstract Syntax Tree Annotation
 *
 * @author John Ericksen
 */
public interface ASTAnnotation {

    /**
     * Getter for a given annotation property
     *
     * @param name
     * @param type
     * @param <T>
     * @return annotation property identified by name with the given type
     */
    <T> T getProperty(String name, Class<T> type);

    /**
     * Getter for the name of the current annotation
     *
     * @return annotation name
     */
    ASTType getASTType();

    /**
     * Getter for the properties contained in this annotation
     *
     * @return property names
     */
    ImmutableSet<String> getPropertyNames();
}
