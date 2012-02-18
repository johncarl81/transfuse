package org.androidtransfuse.gen.variableBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.InjectionVariableBuilder;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ApplicationVariableBuilder implements VariableBuilder {

    private static final String GET_APPLICATION = "getApplication";

    private InjectionNode contextInjectionNode;
    private InjectionVariableBuilder injectionVariableBuilder;

    @Inject
    public ApplicationVariableBuilder(@Assisted InjectionNode contextInjectionNode, InjectionVariableBuilder injectionVariableBuilder) {
        this.contextInjectionNode = contextInjectionNode;
        this.injectionVariableBuilder = injectionVariableBuilder;
    }

    @Override
    public JExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        JExpression contextVar = injectionVariableBuilder.buildVariable(injectionBuilderContext, contextInjectionNode);

        return contextVar.invoke(GET_APPLICATION);
    }
}

