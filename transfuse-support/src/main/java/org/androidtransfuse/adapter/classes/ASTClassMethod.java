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
package org.androidtransfuse.adapter.classes;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.androidtransfuse.adapter.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Class specific AST Method
 *
 * @author John Ericksen
 */
public class ASTClassMethod implements ASTMethod {

    private final Method method;
    private final ImmutableList<ASTParameter> parameters;
    private final ASTType returnType;
    private final ASTAccessModifier modifier;
    private final ImmutableSet<ASTAnnotation> annotations;
    private final ImmutableSet<ASTType> throwTypes;

    public ASTClassMethod(Method method,
                          ASTType returnType,
                          ImmutableList<ASTParameter> parameters,
                          ASTAccessModifier modifier,
                          ImmutableSet<ASTAnnotation> annotations,
                          ImmutableSet<ASTType> throwTypes) {
        this.method = method;
        this.parameters = parameters;
        this.returnType = returnType;
        this.modifier = modifier;
        this.annotations = annotations;
        this.throwTypes = throwTypes;
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotation) {
        return method.getAnnotation(annotation);
    }

    @Override
    public boolean isAnnotated(Class<? extends Annotation> annotation) {
        return method.isAnnotationPresent(annotation);
    }

    @Override
    public String getName() {
        return method.getName();
    }

    @Override
    public List<ASTParameter> getParameters() {
        return parameters;
    }

    @Override
    public ASTType getReturnType() {
        return returnType;
    }

    @Override
    public ImmutableSet<ASTAnnotation> getAnnotations() {
        return annotations;
    }

    public ASTAccessModifier getAccessModifier() {
        return modifier;
    }

    @Override
    public ImmutableSet<ASTType> getThrowsTypes() {
        return throwTypes;
    }

    @Override
    public ASTAnnotation getASTAnnotation(Class<? extends Annotation> annotation) {
        return ASTUtils.getInstance().getAnnotation(annotation, getAnnotations());
    }
}
