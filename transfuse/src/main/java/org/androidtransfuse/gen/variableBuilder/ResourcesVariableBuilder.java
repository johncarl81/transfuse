package org.androidtransfuse.gen.variableBuilder;

import android.content.res.Resources;
import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.InjectionExpressionBuilder;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;

public class ResourcesVariableBuilder extends ConsistentTypeVariableBuilder {

    private static final String GET_RESOURCES = "getResources";
    private InjectionNode applicationInjectionNode;
    private InjectionExpressionBuilder injectionExpressionBuilder;

    @Inject
    public ResourcesVariableBuilder(@Assisted InjectionNode applicationInjectionNode,
                                    InjectionExpressionBuilder injectionExpressionBuilder,
                                    TypedExpressionFactory typedExpressionFactory) {
        super(Resources.class, typedExpressionFactory);
        this.applicationInjectionNode = applicationInjectionNode;
        this.injectionExpressionBuilder = injectionExpressionBuilder;
    }

    @Override
    public JExpression buildExpression(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        TypedExpression contextVar = injectionExpressionBuilder.buildVariable(injectionBuilderContext, applicationInjectionNode);

        return contextVar.getExpression().invoke(GET_RESOURCES);
    }
}