package org.androidtransfuse.gen.variableBuilder;

import android.app.Application;
import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.InjectionExpressionBuilder;
import org.androidtransfuse.gen.TypedExpression;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ApplicationVariableBuilder extends ConsistentTypeVariableBuilder {

    private static final String GET_APPLICATION = "getApplication";

    private InjectionNode contextInjectionNode;
    private InjectionExpressionBuilder injectionExpressionBuilder;

    @Inject
    public ApplicationVariableBuilder(@Assisted InjectionNode contextInjectionNode, InjectionExpressionBuilder injectionExpressionBuilder) {
        super(Application.class);
        this.contextInjectionNode = contextInjectionNode;
        this.injectionExpressionBuilder = injectionExpressionBuilder;
    }

    @Override
    public JExpression buildExpression(InjectionBuilderContext context, InjectionNode injectionNode) {
        TypedExpression contextVar = injectionExpressionBuilder.buildVariable(context, contextInjectionNode);

        return contextVar.getExpression().invoke(GET_APPLICATION);
    }
}

