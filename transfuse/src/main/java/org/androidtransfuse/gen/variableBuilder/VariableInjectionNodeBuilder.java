package org.androidtransfuse.gen.variableBuilder;

import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.Analyzer;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class VariableInjectionNodeBuilder extends InjectionNodeBuilderNoAnnotationAdapter {

    private Analyzer analyzer;
    private Provider<VariableInjectionBuilder> variableInjectionBuilderProvider;

    @Inject
    public VariableInjectionNodeBuilder(Analyzer analyzer, Provider<VariableInjectionBuilder> variableInjectionBuilderProvider) {
        this.analyzer = analyzer;
        this.variableInjectionBuilderProvider = variableInjectionBuilderProvider;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context) {
        InjectionNode injectionNode = analyzer.analyze(astType, astType, context);

        injectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderProvider.get());

        return injectionNode;
    }
}
