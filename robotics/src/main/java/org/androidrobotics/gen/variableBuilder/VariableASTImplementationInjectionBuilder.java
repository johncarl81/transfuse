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

    private ASTType implType;

    @Inject
    public VariableASTImplementationInjectionBuilder(@Assisted ASTType implType,
                                                     JCodeModel codeModel,
                                                     UniqueVariableNamer variableNamer,
                                                     InjectionInvocationBuilder injectionInvocationBuilder) {
        super(codeModel, variableNamer, injectionInvocationBuilder);

        this.implType = implType;
    }

    @Override
    public JExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        return innerBuildVariable(implType.getName(), injectionBuilderContext, injectionNode);
    }
}
