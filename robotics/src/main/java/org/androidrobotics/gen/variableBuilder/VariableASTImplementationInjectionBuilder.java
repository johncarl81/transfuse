package org.androidrobotics.gen.variableBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpression;
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

    @Inject
    public VariableASTImplementationInjectionBuilder(@Assisted ASTType astType,
                                                     JCodeModel codeModel,
                                                     UniqueVariableNamer variableNamer,
                                                     InjectionInvocationBuilder injectionInvocationBuilder) {
        super(codeModel, variableNamer, injectionInvocationBuilder);

        this.astType = astType;
    }

    @Override
    public JExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        return innerBuildVariable(astType.getName(), injectionBuilderContext, injectionNode);
    }
}
