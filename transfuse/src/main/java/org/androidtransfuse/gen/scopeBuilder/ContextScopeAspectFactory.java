package org.androidtransfuse.gen.scopeBuilder;

import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
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
