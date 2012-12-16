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
package org.androidtransfuse.analysis;

import com.google.inject.assistedinject.Assisted;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.repository.AOPRepository;
import org.androidtransfuse.analysis.repository.AnalysisRepository;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * @author John Ericksen
 */
public class AnalysisContext {

    private final Map<ASTType, InjectionNode> dependents = new HashMap<ASTType, InjectionNode>();
    private final Stack<InjectionNode> dependencyHistory;
    private final AnalysisRepository analysisRepository;
    private final InjectionNodeBuilderRepository injectionNodeBuilders;
    private final AOPRepository aopRepository;

    @Inject
    public AnalysisContext(@Assisted InjectionNodeBuilderRepository injectionNodeBuilders, AnalysisRepository analysisRepository, AOPRepository aopRepository) {
        this.dependencyHistory = new Stack<InjectionNode>();
        this.analysisRepository = analysisRepository;
        this.injectionNodeBuilders = injectionNodeBuilders;
        this.aopRepository = aopRepository;
    }

    private AnalysisContext(InjectionNode node, AnalysisContext previousContext, AnalysisRepository analysisRepository, InjectionNodeBuilderRepository injectionNodeBuilders, AOPRepository aopRepository) {
        this(injectionNodeBuilders, analysisRepository, aopRepository);
        this.dependents.putAll(previousContext.dependents);
        this.dependents.put(node.getASTType(), node);
        this.dependencyHistory.addAll(previousContext.dependencyHistory);
        this.dependencyHistory.push(node);
    }

    public AnalysisContext addDependent(InjectionNode node) {
        return new AnalysisContext(node, this, analysisRepository, injectionNodeBuilders, aopRepository);
    }

    public boolean isDependent(ASTType astType) {
        return dependents.containsKey(astType);
    }

    public InjectionNode getInjectionNode(ASTType astType) {
        return dependents.get(astType);
    }

    public AnalysisRepository getAnalysisRepository() {
        return analysisRepository;
    }

    public InjectionNodeBuilderRepository getInjectionNodeBuilders() {
        return injectionNodeBuilders;
    }

    public AOPRepository getAOPRepository() {
        return aopRepository;
    }

    public Stack<InjectionNode> getDependencyHistory() {
        Stack<InjectionNode> dependencyHistoryCopy = new Stack<InjectionNode>();
        dependencyHistoryCopy.addAll(dependencyHistory);
        return dependencyHistoryCopy;
    }
}
