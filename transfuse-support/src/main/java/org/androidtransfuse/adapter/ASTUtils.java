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

/**
 * Utility singleton for AST classes.
 *
 * @author John Ericksen
 */
public final class ASTUtils {

    private static final ASTUtils INSTANCE = new ASTUtils();

    private ASTUtils() {
        //private singleton constructor
    }

    public static ASTUtils getInstance() {
        return INSTANCE;
    }

    /**
     * Determines if the given ASTType inherits or extends from the given inheritable ASTType
     *
     * @param astType     target
     * @param inheritable inheritance target
     * @param implement flag to trigger the method to search for implements inheritance
     * @param extend flag to trigger the method to search for extends inheritance
     * @return true if the given astType target inherits from the inheritable type with the given rules.
     */
    public boolean inherits(ASTType astType, ASTType inheritable, boolean implement, boolean extend) {
        if (astType == null) {
            return false;
        }
        if (astType.equals(inheritable)) {
            return true;
        }
        if (implement) {
            for (ASTType typeInterfaces : astType.getInterfaces()) {
                if (inherits(typeInterfaces, inheritable, implement, extend)) {
                    return true;
                }
            }
        }
        return extend && inherits(astType.getSuperClass(), inheritable, implement, extend);
    }

    public ASTAnnotation getAnnotation(Class annotationClass, ImmutableSet<ASTAnnotation> annotations) {
        return getAnnotation(annotationClass.getCanonicalName(), annotations);
    }

    public ASTAnnotation getAnnotation(String annotationClassName, ImmutableSet<ASTAnnotation> annotations){
        for (ASTAnnotation astAnnotation : annotations) {
            if (astAnnotation.getASTType().getName().equals(annotationClassName)) {
                return astAnnotation;
            }
        }
        return null;
    }

    public boolean isAnnotated(ASTType type, Class annotationClass){
        return isAnnotated(type, annotationClass.getCanonicalName());
    }

    public boolean isAnnotated(ASTType type, String annotationClassName){
        return getAnnotation(annotationClassName, type.getAnnotations()) != null;
    }
}
