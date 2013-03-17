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
package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.gen.scopeBuilder.ScopeAspectFactory;
import org.androidtransfuse.model.InjectionNode;

import java.util.Collection;

/**
 * Analysis to determine if the given type is scoped.
 *
 * @author John Ericksen
 */
public class ScopeAnalysis extends ASTAnalysisAdaptor {

    @Override
    public void analyzeType(InjectionNode injectionNode, ASTType concreteType, AnalysisContext context) {

        if (injectionNode.getASTType().equals(concreteType)) {

            ASTType scopedType = context.getInjectionNodeBuilders().getScope(injectionNode.getASTType());
            if(scopedType != null){
                ScopeAspectFactory scopeAspectFactory = context.getInjectionNodeBuilders().getScopeAspectFactory(scopedType);
                injectionNode.addAspect(scopeAspectFactory.buildAspect(injectionNode, concreteType, context));
                return;
            }

            for (ASTType scopeType : context.getInjectionNodeBuilders().getScopes()) {
                Collection<ASTAnnotation> annotations = concreteType.getAnnotations();
                //todo: clean this up
                for (ASTAnnotation annotation : annotations) {
                    if(annotation.getASTType().equals(scopeType)){
                        ScopeAspectFactory scopeAspectFactory = context.getInjectionNodeBuilders().getScopeAspectFactory(scopeType);
                        injectionNode.addAspect(scopeAspectFactory.buildAspect(injectionNode, concreteType, context));
                        return;
                    }
                }
            }
        }
    }
}
