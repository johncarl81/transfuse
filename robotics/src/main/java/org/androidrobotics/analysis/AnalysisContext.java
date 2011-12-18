package org.androidrobotics.analysis;

import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.gen.VariableBuilderRepository;
import org.androidrobotics.model.InjectionNode;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class AnalysisContext {

    private Map<ASTType, InjectionNode> dependents;
    private AnalysisRepository analysisRepository;
    private VariableBuilderRepository variableBuilders;

    public AnalysisContext(AnalysisRepository analysisRepository, VariableBuilderRepository variableBuilders) {
        this.dependents = Collections.emptyMap();
        this.analysisRepository = analysisRepository;
        this.variableBuilders = variableBuilders;
    }

    private AnalysisContext(ASTType dependent, InjectionNode node, AnalysisContext previousContext, AnalysisRepository analysisRepository, VariableBuilderRepository variableBuilders) {
        this(analysisRepository, variableBuilders);
        this.dependents = new HashMap<ASTType, InjectionNode>();
        this.dependents.putAll(previousContext.dependents);
        this.dependents.put(dependent, node);
    }

    public AnalysisContext addDependent(ASTType dependent, InjectionNode node) {
        return new AnalysisContext(dependent, node, this, analysisRepository, variableBuilders);
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

    public VariableBuilderRepository getVariableBuilders() {
        return variableBuilders;
    }
}
