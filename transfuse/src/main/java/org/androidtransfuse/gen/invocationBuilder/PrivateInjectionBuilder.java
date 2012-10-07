package org.androidtransfuse.gen.invocationBuilder;

import com.sun.codemodel.*;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.ConstructorInjectionPoint;
import org.androidtransfuse.model.FieldInjectionPoint;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;
import org.androidtransfuse.util.InjectionUtil;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * Injection Builder for building privately scoped elements.
 *
 * @author John Ericksen
 */
public class PrivateInjectionBuilder implements ModifierInjectionBuilder {

    private JCodeModel codeModel;
    private TypeInvocationHelper invocationHelper;

    @Inject
    public PrivateInjectionBuilder(JCodeModel codeModel, TypeInvocationHelper invocationHelper) {
        this.codeModel = codeModel;
        this.invocationHelper = invocationHelper;
    }

    @Override
    public JExpression buildConstructorCall(Map<InjectionNode, TypedExpression> expressionMap, ConstructorInjectionPoint constructorInjectionPoint, JType type) {

        //InjectionUtil.setConstructor(Class<T> targetClass, Class[] argClasses,Object[] args)
        JInvocation constructorInvocation = codeModel.ref(InjectionUtil.class).staticInvoke(InjectionUtil.GET_INSTANCE_METHOD).invoke(InjectionUtil.CALL_CONSTRUCTOR_METHOD)
                .arg(codeModel.ref(type.fullName()).dotclass());

        //add classes
        JArray classArray = JExpr.newArray(codeModel.ref(Class.class));
        for (InjectionNode injectionNode : constructorInjectionPoint.getInjectionNodes()) {
            classArray.add(codeModel.ref(injectionNode.getUsageType().getName()).dotclass());
        }
        constructorInvocation.arg(classArray);

        //add args
        constructorInvocation.arg(buildArgs(expressionMap, constructorInjectionPoint.getInjectionNodes()));

        return constructorInvocation;
    }

    @Override
    public <T> JInvocation buildMethodCall(String returnType, Map<T, TypedExpression> expressionMap, String methodName, List<T> injectionNodes, List<ASTType> injectionNodeType, ASTType targetExpressionType, JExpression targetExpression) {

        JClass targetType = codeModel.ref(targetExpressionType.getName());
        //InjectionUtil.getInstance().setMethod(Class targetClass, Object target, String method, Class[] argClasses,Object[] args)
        JInvocation methodInvocation = codeModel.ref(InjectionUtil.class).staticInvoke(InjectionUtil.GET_INSTANCE_METHOD).invoke(InjectionUtil.CALL_METHOD_METHOD)
                .arg(codeModel.ref(returnType).dotclass())
                .arg(targetType.dotclass())
                .arg(targetExpression)
                .arg(methodName);

        //add classes
        JArray classArray = JExpr.newArray(codeModel.ref(Class.class));
        for (ASTType injectionNode : injectionNodeType) {
            classArray.add(codeModel.ref(injectionNode.getName()).dotclass());
        }
        methodInvocation.arg(classArray);

        //add args
        methodInvocation.arg(buildArgs(expressionMap, injectionNodes));

        return methodInvocation;
    }

    @Override
    public JExpression buildFieldGet(ASTType returnType, ASTType variableType, JExpression variable, String name) {
        return codeModel.ref(InjectionUtil.class).staticInvoke(InjectionUtil.GET_INSTANCE_METHOD).invoke(InjectionUtil.GET_FIELD_METHOD)
                .arg(codeModel.ref(returnType.getName()).dotclass())
                .arg(codeModel.ref(variableType.getName()).dotclass())
                .arg(variable)
                .arg(name);
    }

    @Override
    public JStatement buildFieldSet(TypedExpression expression, FieldInjectionPoint fieldInjectionPoint, JExpression variable) {
        JClass variableType = codeModel.ref(fieldInjectionPoint.getContainingType().getName());

        return codeModel.ref(InjectionUtil.class).staticInvoke(InjectionUtil.GET_INSTANCE_METHOD).invoke(InjectionUtil.SET_FIELD_METHOD)
                .arg(variableType.dotclass())
                .arg(variable)
                .arg(fieldInjectionPoint.getName())
                .arg(expression.getExpression());
    }

    private <T> JArray buildArgs(Map<T, TypedExpression> expressionMap, List<T> keys){
        JArray argArray = JExpr.newArray(codeModel.ref(Object.class));
        for (T key : keys) {
            argArray.add(invocationHelper.getExpression(null, expressionMap, key));
        }
        return argArray;
    }
}
