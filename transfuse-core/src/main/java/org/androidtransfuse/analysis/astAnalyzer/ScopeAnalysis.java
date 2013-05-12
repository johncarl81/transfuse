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

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.gen.scopeBuilder.ScopeAspectFactory;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.util.ScopePredicate;
import org.androidtransfuse.validation.Validator;

import javax.inject.Inject;

/**
 * Analysis to determine if the given type is scoped.
 *
 * @author John Ericksen
 */
public class ScopeAnalysis extends ASTAnalysisAdaptor {

    private final ScopePredicate scopePredicate;
    private final Validator validator;

    @Inject
    public ScopeAnalysis(ScopePredicate scopePredicate, Validator validator) {
        this.scopePredicate = scopePredicate;
        this.validator = validator;
    }

    @Override
    public void analyzeType(InjectionNode injectionNode, ASTType concreteType, AnalysisContext context) {

        if (injectionNode.getASTType().equals(concreteType)) {

            //type is recognized under a given scope
            ASTType scopedType = context.getInjectionNodeBuilders().getScoped(injectionNode.getTypeSignature());
            if(scopedType != null){
                ScopeAspectFactory scopeAspectFactory = context.getInjectionNodeBuilders().getScopeAspectFactory(scopedType);
                injectionNode.addAspect(scopeAspectFactory.buildAspect(context));
            }
            else{
                //annotated type
                ImmutableSet<ASTAnnotation> scopeAnnotations = FluentIterable.from(concreteType.getAnnotations()).filter(scopePredicate).toSet();

                if(scopeAnnotations.size() > 1){
                    validator.error("Only one scoping may be defined")
                            .element(concreteType).build();
                }
                for (ASTAnnotation scopeAnnotation : scopeAnnotations) {
                    if(context.getInjectionNodeBuilders().containsScope(scopeAnnotation)){
                        ScopeAspectFactory scopeAspectFactory = context.getInjectionNodeBuilders().getScopeAspectFactory(scopeAnnotation.getASTType());
                        injectionNode.addAspect(scopeAspectFactory.buildAspect(context));
                    }
                }
            }
        }
    }
}
