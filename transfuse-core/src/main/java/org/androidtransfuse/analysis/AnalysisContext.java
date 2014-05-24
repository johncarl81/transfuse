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
package org.androidtransfuse.analysis;

import com.google.common.collect.ImmutableMap;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;
import java.util.Collection;

/**
 * @author John Ericksen
 */
public class AnalysisContext {

    private final ImmutableMap<ASTType, InjectionNode> dependents;
    private final InjectionNodeBuilderRepository injectionNodeBuilders;

    @Inject
    public AnalysisContext(/*@Assisted*/ InjectionNodeBuilderRepository injectionNodeBuilders) {
        this.dependents = ImmutableMap.of();
        this.injectionNodeBuilders = injectionNodeBuilders;
    }

    private AnalysisContext(InjectionNode node, AnalysisContext previousContext, InjectionNodeBuilderRepository injectionNodeBuilders) {
        ImmutableMap.Builder<ASTType, InjectionNode> dependentsBuilder = ImmutableMap.builder();

        dependentsBuilder.putAll(previousContext.dependents);
        if(!previousContext.dependents.containsKey(node.getASTType())){
            //avoid adding duplicate keys (result of dependency loops)
            dependentsBuilder.put(node.getASTType(), node);
        }

        this.dependents = dependentsBuilder.build();

        this.injectionNodeBuilders = injectionNodeBuilders;
    }

    public AnalysisContext addDependent(InjectionNode node) {
        return new AnalysisContext(node, this, injectionNodeBuilders);
    }

    public boolean isDependent(ASTType astType) {
        return dependents.containsKey(astType);
    }

    public InjectionNode getInjectionNode(ASTType astType) {
        return dependents.get(astType);
    }

    public InjectionNodeBuilderRepository getInjectionNodeBuilders() {
        return injectionNodeBuilders;
    }

    public Collection<InjectionNode> getDependencyHistory() {
        return dependents.values();
    }
}
