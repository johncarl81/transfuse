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

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTParameter;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.ASTUtils;

import java.lang.annotation.Annotation;

/**
 * Class specific AST Parameter
 *
 * @author John Ericksen
 */
public class ASTClassParameter implements ASTParameter {

    private final String name;
    private final ASTType astType;
    private final ImmutableMap<Class<? extends Annotation>, Annotation> annotationMap;
    private final ImmutableSet<ASTAnnotation> annotations;

    public ASTClassParameter(String name, Annotation[] annotations, ASTType astType, ImmutableSet<ASTAnnotation> astAnnotations) {
        this.name = name;
        this.annotations = astAnnotations;
        this.astType = astType;

        ImmutableMap.Builder<Class<? extends Annotation>, Annotation> classAnnotationBuilder = ImmutableMap.builder();
        for (Annotation annotation : annotations) {
            classAnnotationBuilder.put(annotation.annotationType(), annotation);
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
        return name;
    }

    @Override
    public ASTType getASTType() {
        return astType;
    }

    @Override
    public ImmutableSet<ASTAnnotation> getAnnotations() {
        return annotations;
    }

    @Override
    public ASTAnnotation getASTAnnotation(Class<? extends Annotation> annotation) {
        return ASTUtils.getInstance().getAnnotation(annotation, getAnnotations());
    }
}
