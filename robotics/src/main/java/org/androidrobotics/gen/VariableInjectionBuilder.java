package org.androidrobotics.gen;

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpression;
import org.androidrobotics.analysis.AnalysisContext;
import org.androidrobotics.analysis.Analyzer;
import org.androidrobotics.analysis.adapter.ASTType;
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
    public JExpression buildVariable(InjectionBuilderContext injectionBuilderContext) {
        return innerBuildVariable(injectionBuilderContext.getInjectionNode().getClassName(), injectionBuilderContext);
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context) {
        return analyzer.analyze(astType, astType, context);
    }
}
