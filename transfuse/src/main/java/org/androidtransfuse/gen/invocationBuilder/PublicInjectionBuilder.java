package org.androidtransfuse.gen.invocationBuilder;

import com.sun.codemodel.*;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.ConstructorInjectionPoint;
import org.androidtransfuse.model.FieldInjectionPoint;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * Injection Builder for building publicly scoped elements.
 *
 * @author John Ericksen
 */
public class PublicInjectionBuilder implements ModifierInjectionBuilder {

    private TypeInvocationHelper invocationHelper;

    @Inject
    public PublicInjectionBuilder(TypeInvocationHelper invocationHelper) {
        this.invocationHelper = invocationHelper;
    }

    @Override
    public JExpression buildConstructorCall(Map<InjectionNode, TypedExpression> expressionMap, ConstructorInjectionPoint constructorInjectionPoint, JType type) {
        JInvocation constructorInvocation = JExpr._new(type);

        for (InjectionNode node : constructorInjectionPoint.getInjectionNodes()) {
            constructorInvocation.arg(invocationHelper.coerceType(node.getASTType(), expressionMap.get(node)));
        }

        return constructorInvocation;
    }

    @Override
    public <T> JInvocation buildMethodCall(String returnType, Map<T, TypedExpression> expressionMap, String methodName, List<T> parameters, List<ASTType> types, ASTType targetExpressionType, JExpression targetExpression) {
        //public case:
        JInvocation methodInvocation = targetExpression.invoke(methodName);

        for (int i = 0; i < parameters.size(); i++) {
            methodInvocation.arg(invocationHelper.getExpression(types.get(i), expressionMap, parameters.get(i)));
        }

        return methodInvocation;
    }

    @Override
    public JExpression buildFieldGet(ASTType returnType, ASTType variableType, JExpression variable, String name) {
        return variable.ref(name);
    }

    @Override
    public JStatement buildFieldSet(TypedExpression expression, FieldInjectionPoint fieldInjectionPoint, JExpression variable) {
        JBlock assignmentBlock = new JBlock(false, false);

        assignmentBlock.assign(variable.ref(fieldInjectionPoint.getName()), invocationHelper.coerceType(fieldInjectionPoint.getInjectionNode().getASTType(), expression));

        return assignmentBlock;
    }
}
