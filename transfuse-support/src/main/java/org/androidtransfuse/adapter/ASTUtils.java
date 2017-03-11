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

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;

import java.util.List;

/**
 * Utility singleton for AST classes.
 *
 * @author John Ericksen
 */
public final class ASTUtils {

    private static final ASTType OBJECT_TYPE = new ASTStringType(Object.class.getCanonicalName());
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
     * @return true if the given astType target inherits from the inheritable type with the given rules.
     */
    public boolean inherits(ASTType astType, ASTType inheritable) {
        if (astType == null) {
            return false;
        }
        if(inheritable == null || inheritable.equals(OBJECT_TYPE)){
            return true;
        }
        if (astType.equals(inheritable)) {
            return true;
        }
        for (ASTType typeInterfaces : astType.getInterfaces()) {
            if (inherits(typeInterfaces, inheritable)) {
                return true;
            }
        }
        return inherits(astType.getSuperClass(), inheritable);
    }

    public boolean isAnnotated(ASTType annotatationType, ImmutableSet<ASTAnnotation> annotations) {
        return getAnnotation(annotatationType, annotations) != null;
    }

    public ASTAnnotation getAnnotation(ASTType annotationType, ImmutableSet<ASTAnnotation> annotations) {
        return getAnnotation(annotationType.getName(), annotations);
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

    public ASTConstructor findConstructor(ASTType type, ASTType... parameters){
        ASTConstructor foundConstructor = null;
        for (ASTConstructor astConstructor : type.getConstructors()) {
            if (parameterTypesMatch(astConstructor.getParameters(), parameters)) {
                foundConstructor = astConstructor;
            }
        }
        return foundConstructor;
    }

    public boolean constructorExists(ASTType type, final ASTType... parameters){
        return FluentIterable.from(type.getConstructors()).anyMatch(new Predicate<ASTConstructor>() {
            @Override
            public boolean apply(ASTConstructor constructor) {
                return parameterTypesMatch(constructor.getParameters(), parameters);
            }
        });
    }

    public ASTMethod findMethod(ASTType containingType, String methodName, ASTType... methodParameters) {
        MethodSignature matchingSignature = new MethodSignature(methodName, methodParameters);
        for (ASTMethod astMethod : containingType.getMethods()) {
            if(new MethodSignature(astMethod).equals(matchingSignature)){
                return astMethod;
            }
        }
        return null;
    }

    public boolean methodExists(ASTType containingType, String methodName, ASTType... methodParameters) {
        final MethodSignature matchingSignature = new MethodSignature(methodName, methodParameters);
        return FluentIterable.from(containingType.getMethods())
                .transform(new Function<ASTMethod, MethodSignature>() {
                    public MethodSignature apply(ASTMethod astMethod) {
                        return new MethodSignature(astMethod);
                    }
                })
                .anyMatch(new Predicate<MethodSignature>() {
                    public boolean apply(MethodSignature methodSignature) {
                        return methodSignature.equals(matchingSignature);
                    }
                });
    }

    public ASTField findField(ASTType containingType, final String fieldName) {
        for (ASTField astField : containingType.getFields()) {
            if(astField.getName().equals(fieldName)) {
                return astField;
            }
        }
        return null;
    }

    public boolean fieldExists(ASTType containingType, final String fieldName) {
        return FluentIterable.from(containingType.getFields())
                .anyMatch(new Predicate<ASTField>() {
                    public boolean apply(ASTField astField) {
                        return astField.getName().equals(fieldName);
                    }
                });
    }

    public boolean parameterTypesMatch(List<ASTParameter> parameters, ASTType... types){
        boolean matches = true;
        if(parameters.size() == types.length) {
            for (int i = 0; i < parameters.size(); i++) {
                if (!parameters.get(i).getASTType().equals(types[i])) {
                    matches = false;
                }
            }
        }
        else{
            matches = false;
        }
        return matches;
    }
}
