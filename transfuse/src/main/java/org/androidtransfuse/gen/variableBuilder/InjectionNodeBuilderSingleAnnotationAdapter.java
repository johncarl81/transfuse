/**
 * Copyright 2012 John Ericksen
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
package org.androidtransfuse.gen.variableBuilder;

import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.adapter.ASTUtils;
import org.androidtransfuse.model.InjectionNode;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * @author John Ericksen
 */
public abstract class InjectionNodeBuilderSingleAnnotationAdapter implements InjectionNodeBuilder {

    private final Class<? extends Annotation> annotationClass;

    public InjectionNodeBuilderSingleAnnotationAdapter(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context, Collection<ASTAnnotation> annotations) {

        ASTAnnotation annotation = ASTUtils.getInstance().getAnnotation(annotationClass, annotations);

        return buildInjectionNode(astType, context, annotation);
    }

    public abstract InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context, ASTAnnotation annotation);
}
