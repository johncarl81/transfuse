package org.androidtransfuse.gen.invocationBuilder;

import com.sun.codemodel.JExpression;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JStatement;
import com.sun.codemodel.JType;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.ConstructorInjectionPoint;
import org.androidtransfuse.model.FieldInjectionPoint;
import org.androidtransfuse.model.TypedExpression;

import java.util.List;

/**
 * @author John Ericksen
 */
public interface ModifierInjectionBuilder {

    JExpression buildConstructorCall(ConstructorInjectionPoint constructorInjectionPoint, Iterable<JExpression> parameters, JType type);

    JExpression buildFieldGet(ASTType returnType, ASTType variableType, JExpression variable, String name);

    JStatement buildFieldSet(TypedExpression expression, FieldInjectionPoint fieldInjectionPoint, JExpression variable);

    JInvocation buildMethodCall(ASTType returnType, String methodName, Iterable<JExpression> parameters, List<ASTType> injectionNodeType, ASTType targetExpressionType, JExpression targetExpression);
}
