package org.androidrobotics.gen;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpression;
import org.androidrobotics.analysis.AnalysisDependencyProcessingCallback;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class VariableASTImplementationInjectionBuilder extends VariableInjectionBuilderBase {

    private ASTType astType;

    @Inject
    public VariableASTImplementationInjectionBuilder(@Assisted ASTType astType, JCodeModel codeModel, UniqueVariableNamer variableNamer, InjectionInvocationBuilder injectionInvocationBuilder) {
        super(codeModel, variableNamer, injectionInvocationBuilder);

        this.astType = astType;
    }

    @Override
    public JExpression buildVariable(InjectionBuilderContext injectionBuilderContext) {
        return innerBuildVariable(astType.getName(), injectionBuilderContext);
    }

    @Override
    public InjectionNode processInjectionNode(ASTType astType, AnalysisDependencyProcessingCallback callback) {
        return callback.processInjectionNode(this.astType);
    }
}
