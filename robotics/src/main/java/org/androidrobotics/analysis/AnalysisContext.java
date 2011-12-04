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
    private VariableBuilderRepository variableBuilderRepository;

    public AnalysisContext(VariableBuilderRepository variableBuilderRepository) {
        this.variableBuilderRepository = variableBuilderRepository;
        this.dependents = Collections.emptyMap();
    }

    private AnalysisContext(ASTType dependent, InjectionNode node, AnalysisContext previousContext, VariableBuilderRepository variableBuilderRepository) {
        this.variableBuilderRepository = variableBuilderRepository;
        this.dependents = new HashMap<ASTType, InjectionNode>();
        this.dependents.putAll(previousContext.dependents);
        this.dependents.put(dependent, node);
    }

    public AnalysisContext addDependent(ASTType dependent, InjectionNode node) {
        return new AnalysisContext(dependent, node, this, variableBuilderRepository);
    }

    public boolean isDependent(ASTType astType) {
        return dependents.containsKey(astType);
    }

    public InjectionNode getInjectionNode(ASTType astType) {
        return dependents.get(astType);
    }

    public VariableBuilderRepository getVariableBuilderRepository() {
        return variableBuilderRepository;
    }
}
