package org.androidrobotics.analysis;

import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.gen.InjectionNodeBuilderRepository;
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
    private InjectionNodeBuilderRepository injectionNodeBuilders;
    private InterceptorRepository interceptorRepository;
    private AOPRepository aopRepository;

    public AnalysisContext(AnalysisRepository analysisRepository, InjectionNodeBuilderRepository injectionNodeBuilders, InterceptorRepository interceptorRepository, AOPRepository aopRepository) {
        this.dependents = Collections.emptyMap();
        this.analysisRepository = analysisRepository;
        this.injectionNodeBuilders = injectionNodeBuilders;
        this.interceptorRepository = interceptorRepository;
        this.aopRepository = aopRepository;
    }

    private AnalysisContext(ASTType dependent, InjectionNode node, AnalysisContext previousContext, AnalysisRepository analysisRepository, InjectionNodeBuilderRepository injectionNodeBuilders, InterceptorRepository interceptorRepository, AOPRepository aopRepository) {
        this(analysisRepository, injectionNodeBuilders, interceptorRepository, aopRepository);
        this.dependents = new HashMap<ASTType, InjectionNode>();
        this.dependents.putAll(previousContext.dependents);
        this.dependents.put(dependent, node);
    }

    public AnalysisContext addDependent(ASTType dependent, InjectionNode node) {
        return new AnalysisContext(dependent, node, this, analysisRepository, injectionNodeBuilders, interceptorRepository, aopRepository);
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

    public InterceptorRepository getInterceptorRepository() {
        return interceptorRepository;
    }

    public AOPRepository getAopRepository() {
        return aopRepository;
    }
}
