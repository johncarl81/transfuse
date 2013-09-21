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
import java.lang.reflect.Constructor;
import java.util.List;

/**
 * Class specific AST Constructor
 *
 * @author John Ericksen
 */
public class ASTClassConstructor implements ASTConstructor {

    private final Constructor constructor;
    private final ImmutableList<ASTParameter> parameters;
    private final ASTAccessModifier modifier;
    private final ImmutableSet<ASTAnnotation> annotations;
    private final ImmutableSet<ASTType> throwsTypes;

    public ASTClassConstructor(ImmutableSet<ASTAnnotation> annotations, Constructor<?> constructor, ImmutableList<ASTParameter> parameters, ASTAccessModifier modifier, ImmutableSet<ASTType> throwsTypes) {
        this.annotations = annotations;
        this.constructor = constructor;
        this.parameters = parameters;
        this.modifier = modifier;
        this.throwsTypes = throwsTypes;
    }

    @Override
    public boolean isAnnotated(Class<? extends Annotation> annotation) {
        return constructor.isAnnotationPresent(annotation);
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotation) {
        return (A) constructor.getAnnotation(annotation);
    }

    @Override
    public List<ASTParameter> getParameters() {
        return parameters;
    }

    @Override
    public String getName() {
        return constructor.getName();
    }

    @Override
    public ImmutableSet<ASTAnnotation> getAnnotations() {
        return annotations;
    }

    @Override
    public ASTAccessModifier getAccessModifier() {
        return modifier;
    }

    @Override
    public ImmutableSet<ASTType> getThrowsTypes() {
        return throwsTypes;
    }

    @Override
    public ASTAnnotation getASTAnnotation(Class<? extends Annotation> annotation) {
        return ASTUtils.getInstance().getAnnotation(annotation, getAnnotations());
    }
}
