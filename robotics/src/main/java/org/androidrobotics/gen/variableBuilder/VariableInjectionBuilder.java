package org.androidrobotics.gen.variableBuilder;

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpression;
import org.androidrobotics.gen.InjectionBuilderContext;
import org.androidrobotics.gen.InjectionInvocationBuilder;
import org.androidrobotics.gen.UniqueVariableNamer;
import org.androidrobotics.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class VariableInjectionBuilder extends VariableInjectionBuilderBase {

    @Inject
    public VariableInjectionBuilder(JCodeModel codeModel, UniqueVariableNamer variableNamer, InjectionInvocationBuilder injectionInvocationBuilder) {
        super(codeModel, variableNamer, injectionInvocationBuilder);
    }

    @Override
    public JExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        return innerBuildVariable(injectionNode.getClassName(), injectionBuilderContext, injectionNode);
    }
}
