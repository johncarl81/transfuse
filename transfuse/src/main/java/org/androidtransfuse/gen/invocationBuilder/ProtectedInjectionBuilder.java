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
 * @author John Ericksen
 */
public class ProtectedInjectionBuilder implements ModifierInjectionBuilder {

    private PrivateInjectionBuilder delegate;

    @Inject
    public ProtectedInjectionBuilder(PrivateInjectionBuilder delegate) {
        this.delegate = delegate;
    }

    @Override
    public JExpression buildConstructorCall(Map<InjectionNode, TypedExpression> expressionMap, ConstructorInjectionPoint constructorInjectionPoint, JType type) {
        return delegate.buildConstructorCall(expressionMap, constructorInjectionPoint, type);
    }

    @Override
    public <T> JInvocation buildMethodCall(String returnType, Map<T, TypedExpression> expressionMap, String methodName, List<T> injectionNodes, List<ASTType> injectionNodeType, ASTType targetExpressionType, JExpression targetExpression) {
        return delegate.buildMethodCall(returnType, expressionMap, methodName, injectionNodes, injectionNodeType, targetExpressionType, targetExpression);
    }

    @Override
    public JExpression buildFieldGet(ASTType returnType, JClass variableType, JExpression variable, String name) {
        return delegate.buildFieldGet(returnType, variableType, variable, name);
    }

    @Override
    public JStatement buildFieldSet(TypedExpression expression, FieldInjectionPoint fieldInjectionPoint, JExpression variable) {
        return delegate.buildFieldSet(expression, fieldInjectionPoint, variable);
    }
}
