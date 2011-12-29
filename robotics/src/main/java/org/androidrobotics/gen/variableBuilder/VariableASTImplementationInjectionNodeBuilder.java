package org.androidrobotics.gen.variableBuilder;

import com.google.inject.assistedinject.Assisted;
import org.androidrobotics.analysis.AnalysisContext;
import org.androidrobotics.analysis.Analyzer;
import org.androidrobotics.analysis.adapter.ASTAnnotation;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class VariableASTImplementationInjectionNodeBuilder implements InjectionNodeBuilder {

    private Analyzer analyzer;
    private ASTType implType;

    @Inject
    public VariableASTImplementationInjectionNodeBuilder(@Assisted ASTType implType, Analyzer analyzer) {
        this.analyzer = analyzer;
        this.implType = implType;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context, ASTAnnotation annotation) {
        return analyzer.analyze(astType, implType, context);
    }
}
