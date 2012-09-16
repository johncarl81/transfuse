package org.androidtransfuse.gen.variableBuilder;

import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.Analyzer;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class VariableInjectionNodeBuilder extends InjectionNodeBuilderNoAnnotationAdapter {

    private final Analyzer analyzer;
    private final VariableInjectionBuilder variableInjectionBuilder;

    @Inject
    public VariableInjectionNodeBuilder(Analyzer analyzer, VariableInjectionBuilder variableInjectionBuilder) {
        this.analyzer = analyzer;
        this.variableInjectionBuilder = variableInjectionBuilder;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context) {
        InjectionNode injectionNode = analyzer.analyze(astType, astType, context);

        injectionNode.addAspect(VariableBuilder.class, variableInjectionBuilder);

        return injectionNode;
    }
}
