package org.androidtransfuse.gen.variableBuilder;

import com.sun.codemodel.JExpression;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.model.InjectionNode;

/**
 * @author John Ericksen
 */
public class ApplicationVariableBuilder implements VariableBuilder {

    private static final String GET_APPLICATION = "getApplication";

    private InjectionNode contextInjectionNode;

    public ApplicationVariableBuilder(InjectionNode contextInjectionNode) {
        this.contextInjectionNode = contextInjectionNode;
    }

    @Override
    public JExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        JExpression contextVar = injectionBuilderContext.buildVariable(contextInjectionNode);

        return contextVar.invoke(GET_APPLICATION);
    }
}

