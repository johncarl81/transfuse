package org.androidtransfuse.gen.invocationBuilder;

import com.sun.codemodel.*;
import org.androidtransfuse.analysis.adapter.ASTParameter;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.ConstructorInjectionPoint;
import org.androidtransfuse.model.FieldInjectionPoint;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

import java.util.List;
import java.util.Map;

/**
 * @author John Ericksen
 */
public interface ModifierInjectionBuilder {

    JExpression buildConstructorCall(Map<InjectionNode, TypedExpression> expressionMap, ConstructorInjectionPoint constructorInjectionPoint, JType type);

    JExpression buildFieldGet(ASTType returnType, JClass variableType, JExpression variable, String name);

    JStatement buildFieldSet(TypedExpression expression, FieldInjectionPoint fieldInjectionPoint, JExpression variable);

    <T> JInvocation buildMethodCall(String returnType, Map<T, TypedExpression> expressionMap, String methodName, List<T> injectionNodes, List<ASTType> injectionNodeType, ASTType targetExpressionType, JExpression targetExpression);
}
