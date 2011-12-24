package org.androidrobotics.gen;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpression;
import org.androidrobotics.gen.variableBuilder.VariableInjectionBuilderBase;
import org.androidrobotics.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class VariableImplementationInjectionBuilder extends VariableInjectionBuilderBase {

    private Class<?> implementationClass;

    @Inject
    public VariableImplementationInjectionBuilder(@Assisted Class<?> implementationClass,
                                                  JCodeModel codeModel,
                                                  UniqueVariableNamer variableNamer,
                                                  InjectionInvocationBuilder injectionInvocationBuilder) {
        super(codeModel, variableNamer, injectionInvocationBuilder);

        this.implementationClass = implementationClass;
    }

    @Override
    public JExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        return innerBuildVariable(implementationClass.getName(), injectionBuilderContext, injectionNode);
    }
}
