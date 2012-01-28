package org.androidrobotics.gen.variableBuilder;

import org.androidrobotics.analysis.AnalysisContext;
import org.androidrobotics.analysis.Analyzer;
import org.androidrobotics.analysis.adapter.ASTAnnotation;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class VariableInjectionNodeBuilder implements InjectionNodeBuilder {

    private Analyzer analyzer;

    @Inject
    public VariableInjectionNodeBuilder(Analyzer analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context, ASTAnnotation annotation) {
        return analyzer.analyze(astType, astType, context);
    }
}
