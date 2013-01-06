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

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTParameter;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.ASTUtils;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * Class specific AST Parameter
 *
 * @author John Ericksen
 */
public class ASTClassParameter implements ASTParameter {

    private final ASTType astType;
    private final ImmutableMap<Class<?>, Annotation> annotationMap;
    private final ImmutableCollection<ASTAnnotation> annotations;

    public ASTClassParameter(Annotation[] annotations, ASTType astType, ImmutableCollection<ASTAnnotation> astAnnotations) {
        this.annotations = astAnnotations;
        this.astType = astType;

        ImmutableMap.Builder<Class<?>, Annotation> classAnnotationBuilder = ImmutableMap.builder();
        for (Annotation annotation : annotations) {
            classAnnotationBuilder.put(annotation.getClass(), annotation);
        }

        this.annotationMap = classAnnotationBuilder.build();
    }

    public boolean isAnnotated(Class<? extends Annotation> annotation) {
        return annotationMap.containsKey(annotation);
    }

    public <A extends Annotation> A getAnnotation(Class<A> annotation) {
        return (A) annotationMap.get(annotation);
    }

    @Override
    public String getName() {
        return astType.getName();
    }

    @Override
    public ASTType getASTType() {
        return astType;
    }

    @Override
    public Collection<ASTAnnotation> getAnnotations() {
        return annotations;
    }

    @Override
    public ASTAnnotation getASTAnnotation(Class annotation) {
        return ASTUtils.getInstance().getAnnotation(annotation, getAnnotations());
    }
}
