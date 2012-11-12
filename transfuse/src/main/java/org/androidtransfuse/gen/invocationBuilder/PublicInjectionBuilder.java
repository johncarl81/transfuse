package org.androidtransfuse.gen.invocationBuilder;

import com.sun.codemodel.*;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.ConstructorInjectionPoint;
import org.androidtransfuse.model.FieldInjectionPoint;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.List;

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
    public JExpression buildConstructorCall(ConstructorInjectionPoint constructorInjectionPoint, Iterable<JExpression> parameters, JType type) {
        JInvocation constructorInvocation = JExpr._new(type);

        for (JExpression parameter : parameters) {
            constructorInvocation.arg(parameter);
        }

        return constructorInvocation;
    }

    @Override
    public JInvocation buildMethodCall(ASTType returnType, String methodName, Iterable<JExpression> parameters, List<ASTType> types, ASTType targetExpressionType, JExpression targetExpression) {
        //public case:
        JInvocation methodInvocation = targetExpression.invoke(methodName);

        for (JExpression parameter : parameters) {
            methodInvocation.arg(parameter);
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
