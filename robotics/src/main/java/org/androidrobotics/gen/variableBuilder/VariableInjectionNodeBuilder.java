package org.androidrobotics.gen.variableBuilder;

import org.androidrobotics.analysis.AnalysisContext;
import org.androidrobotics.analysis.Analyzer;
import org.androidrobotics.analysis.adapter.ASTAnnotation;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.model.InjectionNode;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class VariableInjectionNodeBuilder implements InjectionNodeBuilder {

    private Provider<VariableInjectionBuilder> variableInjectionBuilderProvider;
    private Analyzer analyzer;

    @Inject
    public VariableInjectionNodeBuilder(Provider<VariableInjectionBuilder> variableInjectionBuilderProvider, Analyzer analyzer) {
        this.variableInjectionBuilderProvider = variableInjectionBuilderProvider;
        this.analyzer = analyzer;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context, ASTAnnotation annotation) {
        InjectionNode injectionNode = analyzer.analyze(astType, astType, context);
        injectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderProvider.get());

        return injectionNode;
    }
}
