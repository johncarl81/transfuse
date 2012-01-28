package org.androidrobotics.analysis;

import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.gen.InjectionNodeBuilderRepository;
import org.androidrobotics.model.InjectionNode;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * @author John Ericksen
 */
public class AnalysisContext {

    private Map<ASTType, InjectionNode> dependents;
    private Stack<InjectionNode> dependencyHistory;
    private AnalysisRepository analysisRepository;
    private InjectionNodeBuilderRepository injectionNodeBuilders;
    private AOPRepository aopRepository;
    private int superClassLevel;

    public AnalysisContext(AnalysisRepository analysisRepository, InjectionNodeBuilderRepository injectionNodeBuilders, AOPRepository aopRepository) {
        this.dependents = Collections.emptyMap();
        this.dependencyHistory = new Stack<InjectionNode>();
        this.analysisRepository = analysisRepository;
        this.injectionNodeBuilders = injectionNodeBuilders;
        this.aopRepository = aopRepository;
        this.superClassLevel = 0;
    }

    private AnalysisContext(InjectionNode node, AnalysisContext previousContext, AnalysisRepository analysisRepository, InjectionNodeBuilderRepository injectionNodeBuilders, AOPRepository aopRepository) {
        this(analysisRepository, injectionNodeBuilders, aopRepository);
        this.dependents = new HashMap<ASTType, InjectionNode>();
        this.dependents.putAll(previousContext.dependents);
        this.dependents.put(node.getASTType(), node);
        this.dependencyHistory.addAll(previousContext.dependencyHistory);
        this.dependencyHistory.push(node);
    }

    private AnalysisContext(Map<ASTType, InjectionNode> dependents, AnalysisRepository analysisRepository, InjectionNodeBuilderRepository injectionNodeBuilders, AOPRepository aopRepository, int superClassLevel) {
        this(analysisRepository, injectionNodeBuilders, aopRepository);
        this.dependents = dependents;
        this.superClassLevel = superClassLevel;
    }

    public AnalysisContext incrementSuperClassLevel() {
        return new AnalysisContext(this.dependents, analysisRepository, injectionNodeBuilders, aopRepository, superClassLevel + 1);
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

    public int getSuperClassLevel() {
        return superClassLevel;
    }

    public Stack<InjectionNode> getDependencyHistory() {
        Stack<InjectionNode> dependencyHistoryCopy = new Stack<InjectionNode>();

        dependencyHistoryCopy.addAll(dependencyHistory);

        return dependencyHistoryCopy;
    }
}
