package org.androidtransfuse.gen.variableBuilder.resource;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JInvocation;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.InjectionExpressionBuilder;
import org.androidtransfuse.gen.variableBuilder.TypedExpressionFactory;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;

public class MethodBasedResourceExpressionBuilder implements ResourceExpressionBuilder {

    private Class returnType;
    private String accessMethod;
    private InjectionNode resourcesInjectionNode;
    private InjectionExpressionBuilder injectionExpressionBuilder;
    private TypedExpressionFactory typedExpressionFactory;

    @Inject
    public MethodBasedResourceExpressionBuilder(@Assisted Class returnType,
                                                @Assisted String accessMethod,
                                                @Assisted InjectionNode resourcesInjectionNode,
                                                InjectionExpressionBuilder injectionExpressionBuilder,
                                                TypedExpressionFactory typedExpressionFactory) {
        this.returnType = returnType;
        this.accessMethod = accessMethod;
        this.resourcesInjectionNode = resourcesInjectionNode;
        this.injectionExpressionBuilder = injectionExpressionBuilder;
        this.typedExpressionFactory = typedExpressionFactory;
    }

    @Override
    public TypedExpression buildExpression(InjectionBuilderContext context, JExpression resourceIdExpr) {
        TypedExpression resourcesVar = injectionExpressionBuilder.buildVariable(context, resourcesInjectionNode);

        JInvocation expression = resourcesVar.getExpression().invoke(accessMethod).arg(resourceIdExpr);

        return typedExpressionFactory.build(returnType, expression);
    }
}