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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

/**
 * Abstract Syntax Tree Type node
 *
 * @author John Ericksen
 */
public interface ASTType extends ASTBase {

    /**
     * Access modifier for this type
     *
     * @return access modifier for constructor
     */
    ASTAccessModifier getAccessModifier();

    /**
     * Supplies all available methods
     *
     * @return available methods
     */
    ImmutableSet<ASTMethod> getMethods();

    /**
     * Supplies all available fields
     *
     * @return fields
     */
    ImmutableSet<ASTField> getFields();

    /**
     * Supplies all available constructors
     *
     * @return constructors
     */
    ImmutableSet<ASTConstructor> getConstructors();

    /**
     * Determines if the given AST type represents a concrete class
     *
     * @return concrete class status
     */
    boolean isConcreteClass();

    /**
     * Determines if the given AST type represents an interface
     *
     * @return concrete class status
     */
    boolean isInterface();

    /**
     * Determines if the given AST type is final
     *
     * @return final status
     */
    boolean isFinal();

    /**
     * Determines if the given AST type is static
     *
     * @return static status
     */
    boolean isStatic();

    /**
     * Determines if the given AST type is an inner class.
     *
     * @return is inner class
     */
    boolean isInnerClass();

    /**
     * Determines if the given AST type represents an enum
     *
     * @return if the type is an enum
     */
    boolean isEnum();

    /**
     * Supplies the super class (by extension) of this type
     *
     * @return supertype
     */
    ASTType getSuperClass();

    /**
     * Supplies the list of implemented interfaces
     *
     * @return interfaces implemented
     */
    ImmutableSet<ASTType> getInterfaces();

    /**
     * Generates a list of the generic type parameters, if they are appropriate for the type and exist.
     *
     * @return generic parameters
     */
    ImmutableList<ASTType> getGenericParameters();

    /**
     * Determines if the current type inherits (extends or implements) from the given type.
     *
     * @param type implementing from
     * @return inheritance
     */
    boolean inheritsFrom(ASTType type);

    /**
     * Determines if the current type extends from the given type.
     *
     * @param type implementing from
     * @return inheritance
     */
    boolean extendsFrom(ASTType type);

    /**
     * Determines if the current type implements the given type.
     *
     * @param type implementing from
     * @return inheritance
     */
    boolean implementsFrom(ASTType type);

    /**
     * Returns a parsed package and class for this type
     *
     * @return package class.
     */
    PackageClass getPackageClass();

    ImmutableSet<ASTType> getInnerTypes();
}
