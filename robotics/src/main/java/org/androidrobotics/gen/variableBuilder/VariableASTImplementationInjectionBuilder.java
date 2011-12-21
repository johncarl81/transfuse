package org.androidrobotics.gen.variableBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpression;
import org.androidrobotics.analysis.AnalysisContext;
import org.androidrobotics.analysis.Analyzer;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.gen.InjectionBuilderContext;
import org.androidrobotics.gen.InjectionInvocationBuilder;
import org.androidrobotics.gen.UniqueVariableNamer;
import org.androidrobotics.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class VariableASTImplementationInjectionBuilder extends VariableInjectionBuilderBase {

    private ASTType astType;
    private Analyzer analyzer;

    @Inject
    public VariableASTImplementationInjectionBuilder(@Assisted ASTType astType, Analyzer analyzer, JCodeModel codeModel, UniqueVariableNamer variableNamer, InjectionInvocationBuilder injectionInvocationBuilder) {
        super(codeModel, variableNamer, injectionInvocationBuilder);

        this.astType = astType;
        this.analyzer = analyzer;
    }

    @Override
    public JExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        return innerBuildVariable(astType.getName(), injectionBuilderContext, injectionNode);
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context) {
        InjectionNode injectionNode = analyzer.analyze(astType, this.astType, context);
        injectionNode.addAspect(VariableBuilder.class, this);

        return injectionNode;
    }
}
