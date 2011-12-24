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
    private VariableInjectionBuilderFactory variableInjectionBuilderFactory;

    @Inject
    public VariableASTImplementationInjectionNodeBuilder(@Assisted ASTType implType,
                                                         Analyzer analyzer,
                                                         VariableInjectionBuilderFactory variableInjectionBuilderFactory) {
        this.analyzer = analyzer;
        this.implType = implType;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context, ASTAnnotation annotation) {
        InjectionNode injectionNode = analyzer.analyze(astType, implType, context);
        injectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderFactory.buildVariableInjectionBuilder(implType));

        return injectionNode;
    }
}
