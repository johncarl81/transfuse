package org.androidrobotics.gen.variableBuilder.resource;

import com.sun.codemodel.JExpression;
import org.androidrobotics.gen.InjectionBuilderContext;

public interface ResourceExpressionBuilder {

    JExpression buildExpression(InjectionBuilderContext injectionBuilderContext, JExpression resourceIdExpr);

}