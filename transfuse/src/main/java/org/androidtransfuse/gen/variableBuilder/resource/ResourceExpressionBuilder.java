package org.androidtransfuse.gen.variableBuilder.resource;

import com.sun.codemodel.JExpression;
import org.androidtransfuse.gen.InjectionBuilderContext;

public interface ResourceExpressionBuilder {

    JExpression buildExpression(InjectionBuilderContext injectionBuilderContext, JExpression resourceIdExpr);

}