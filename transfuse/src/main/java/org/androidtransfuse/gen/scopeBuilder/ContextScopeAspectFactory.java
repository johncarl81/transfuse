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
package org.androidtransfuse.gen.scopeBuilder;

import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.astAnalyzer.ScopeAspect;
import org.androidtransfuse.gen.variableBuilder.VariableInjectionBuilderFactory;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.scope.ContextScopeHolder;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ContextScopeAspectFactory implements ScopeAspectFactory{

    private final VariableInjectionBuilderFactory injectionBuilderFactory;
    private final InjectionPointFactory injectionPointFactory;

    @Inject
    public ContextScopeAspectFactory(VariableInjectionBuilderFactory injectionBuilderFactory,
                                     InjectionPointFactory injectionPointFactory) {
        this.injectionBuilderFactory = injectionBuilderFactory;
        this.injectionPointFactory = injectionPointFactory;
    }

    @Override
    public ScopeAspect buildAspect(InjectionNode injectionNode, ASTType astType, AnalysisContext context) {
        InjectionNode contextScopeHolderInjectionNode = injectionPointFactory.buildInjectionNode(ContextScopeHolder.class, context);

        return new ScopeAspect(injectionBuilderFactory.buildContextScopeVariableBuilder(contextScopeHolderInjectionNode));
    }
}
