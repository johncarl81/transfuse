package org.androidtransfuse.gen.variableBuilder.resource;

import com.sun.codemodel.JExpression;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.model.TypedExpression;

public interface ResourceExpressionBuilder {

    TypedExpression buildExpression(InjectionBuilderContext injectionBuilderContext, JExpression resourceIdExpr);

}