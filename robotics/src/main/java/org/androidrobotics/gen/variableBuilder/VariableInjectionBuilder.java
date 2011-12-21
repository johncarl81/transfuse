package org.androidrobotics.gen.variableBuilder;

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
public class VariableInjectionBuilder extends VariableInjectionBuilderBase {

    private Analyzer analyzer;

    @Inject
    public VariableInjectionBuilder(Analyzer analyzer, JCodeModel codeModel, UniqueVariableNamer variableNamer, InjectionInvocationBuilder injectionInvocationBuilder) {
        super(codeModel, variableNamer, injectionInvocationBuilder);
        this.analyzer = analyzer;
    }

    @Override
    public JExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        return innerBuildVariable(injectionNode.getClassName(), injectionBuilderContext, injectionNode);
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context) {
        InjectionNode injectionNode = analyzer.analyze(astType, astType, context);
        injectionNode.addAspect(VariableBuilder.class, this);

        return injectionNode;
    }
}
