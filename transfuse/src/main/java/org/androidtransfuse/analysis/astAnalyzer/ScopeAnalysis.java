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

import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.repository.ScopeAspectFactoryRepository;
import org.androidtransfuse.gen.scopeBuilder.ScopeAspectFactory;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;
import java.lang.annotation.Annotation;

/**
 * Analysis to determine if the given type is scoped.
 *
 * @author John Ericksen
 */
public class ScopeAnalysis extends ASTAnalysisAdaptor {

    private final ScopeAspectFactoryRepository scopeAspectFactoryRepository;

    @Inject
    public ScopeAnalysis(ScopeAspectFactoryRepository scopeAspectFactoryRepository) {
        this.scopeAspectFactoryRepository = scopeAspectFactoryRepository;
    }

    @Override
    public void analyzeType(InjectionNode injectionNode, ASTType concreteType, AnalysisContext context) {

        if (injectionNode.getASTType().equals(concreteType)) {
            for (Class<? extends Annotation> scopeType : scopeAspectFactoryRepository.getScopes()) {
                if (concreteType.isAnnotated(scopeType)) {
                    ScopeAspectFactory scopeAspectFactory = scopeAspectFactoryRepository.getScopeAspectFactory(scopeType);
                    injectionNode.addAspect(scopeAspectFactory.buildAspect(injectionNode, concreteType, context));
                }
            }
        }
    }
}
