package org.androidtransfuse.gen;

import com.sun.codemodel.*;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.*;
import org.androidtransfuse.model.*;
import org.androidtransfuse.util.InjectionUtil;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class InvocationBuilder {

    private JCodeModel codeModel;
    private ASTClassFactory astClassFactory;

    @Inject
    public InvocationBuilder(JCodeModel codeModel, ASTClassFactory astClassFactory) {
        this.codeModel = codeModel;
        this.astClassFactory = astClassFactory;
    }

    public JInvocation buildMethodCall(String returnType, List<ASTParameter> callingParameters, Map<ASTParameter, TypedExpression> parameters, JExpression targetExpression, ASTMethod methodToCall) throws ClassNotFoundException, JClassAlreadyExistsException {
        List<ASTParameter> matchedParameters = matchMethodArguments(callingParameters, methodToCall);
        List<ASTType> parameterTypes = new ArrayList<ASTType>();

        for (ASTParameter matchedParameter : matchedParameters) {
            parameterTypes.add(matchedParameter.getASTType());
        }

        if (methodToCall.getAccessModifier().equals(ASTAccessModifier.PUBLIC)) {
            //public access
            return buildPublicMethodCall(parameters, methodToCall.getName(), matchedParameters, parameterTypes, targetExpression);
        }
        //non-public access
        //todo:add superclass level
        return buildPrivateMethodCall(returnType, 0, parameters, methodToCall.getName(), matchedParameters, pullASTParameterTypes(matchedParameters), targetExpression);
    }

    private List<ASTType> pullASTParameterTypes(List<ASTParameter> matchedParameters) {
        List<ASTType> astTypes = new ArrayList<ASTType>();

        for (ASTParameter matchedParameter : matchedParameters) {
            astTypes.add(matchedParameter.getASTType());
        }

        return astTypes;
    }

    private List<ASTParameter> matchMethodArguments(List<ASTParameter> parametersToMatch, ASTMethod methodToCall) {
        List<ASTParameter> arguments = new ArrayList<ASTParameter>();

        List<ASTParameter> overrideParameters = new ArrayList<ASTParameter>(parametersToMatch);

        for (ASTParameter callParameter : methodToCall.getParameters()) {
            Iterator<ASTParameter> overrideParameterIterator = overrideParameters.iterator();

            while (overrideParameterIterator.hasNext()) {
                ASTParameter overrideParameter = overrideParameterIterator.next();
                if (overrideParameter.getASTType().equals(callParameter.getASTType())) {
                    arguments.add(overrideParameter);
                    overrideParameterIterator.remove();
                    break;
                }
            }
        }

        return arguments;
    }

    private <T> JExpression getExpression(ASTType type, Map<T, TypedExpression> expressionMap, T parameter) {

        if (parameter == null) {
            return JExpr._null();
        } else if (type != null) {
            return coerceType(type, expressionMap.get(parameter));
        } else {
            return expressionMap.get(parameter).getExpression();
        }
    }

    public JStatement buildMethodCall(String returnType, Map<InjectionNode, TypedExpression> expressionMap, MethodInjectionPoint methodInjectionPoint, JExpression variable) throws ClassNotFoundException, JClassAlreadyExistsException {
        if (ASTAccessModifier.PUBLIC.equals(methodInjectionPoint.getAccessModifier())) {
            List<ASTType> types = new ArrayList<ASTType>();
            for (InjectionNode injectionNode : methodInjectionPoint.getInjectionNodes()) {
                types.add(injectionNode.getASTType());
            }

            return buildPublicMethodCall(expressionMap, methodInjectionPoint.getName(), methodInjectionPoint.getInjectionNodes(), types, variable);
        }
        return buildPrivateMethodCall(returnType, methodInjectionPoint.getSuperClassLevel(), expressionMap, methodInjectionPoint.getName(), methodInjectionPoint.getInjectionNodes(), pullASTTypes(methodInjectionPoint.getInjectionNodes()), variable);
    }

    private List<ASTType> pullASTTypes(List<InjectionNode> injectionNodes) {
        List<ASTType> astTypes = new ArrayList<ASTType>();

        for (InjectionNode injectionNode : injectionNodes) {
            astTypes.add(injectionNode.getASTType());
        }

        return astTypes;
    }

    private <T> JInvocation buildPrivateMethodCall(String returnType, int superClassLevel, Map<T, TypedExpression> expressionMap, String methodName, List<T> injectionNodes, List<ASTType> injectioNodeType, JExpression targetExpression) throws ClassNotFoundException, JClassAlreadyExistsException {

        //InjectionUtil.getInstance().setMethod(Object target, int superLevel, String method, Class[] argClasses,Object[] args)
        JInvocation methodInvocation = codeModel.ref(InjectionUtil.class).staticInvoke(InjectionUtil.GET_INSTANCE_METHOD).invoke(InjectionUtil.CALL_METHOD_METHOD)
                .arg(codeModel.ref(returnType).dotclass())
                .arg(targetExpression)
                .arg(JExpr.lit(superClassLevel))
                .arg(methodName);

        //add classes
        JArray classArray = JExpr.newArray(codeModel.ref(Class.class));
        for (ASTType injectionNode : injectioNodeType) {
            classArray.add(codeModel.ref(injectionNode.getName()).dotclass());
        }
        methodInvocation.arg(classArray);

        //add args
        JArray argArray = JExpr.newArray(codeModel.ref(Object.class));
        for (T injectionNode : injectionNodes) {
            argArray.add(getExpression(null, expressionMap, injectionNode));
        }
        methodInvocation.arg(argArray);

        return methodInvocation;
    }

    public <T> JInvocation buildPublicMethodCall(Map<T, TypedExpression> expressionMap, String methodName, List<T> parameters, List<ASTType> types, JExpression targetExpression) throws ClassNotFoundException, JClassAlreadyExistsException {
        //public case:
        JInvocation methodInvocation = targetExpression.invoke(methodName);

        for (int i = 0; i < parameters.size(); i++) {
            methodInvocation.arg(getExpression(types.get(i), expressionMap, parameters.get(i)));
        }

        return methodInvocation;
    }

    public JStatement buildFieldSet(TypedExpression expression, FieldInjectionPoint fieldInjectionPoint, JExpression variable) throws ClassNotFoundException, JClassAlreadyExistsException {
        if (ASTAccessModifier.PUBLIC.equals(fieldInjectionPoint.getAccessModifier())) {
            return buildPublicFieldSet(expression, fieldInjectionPoint, variable);
        }
        return buildPrivateFieldSet(expression, fieldInjectionPoint, variable);
    }

    private JStatement buildPublicFieldSet(TypedExpression expression, FieldInjectionPoint fieldInjectionPoint, JExpression variable) throws ClassNotFoundException, JClassAlreadyExistsException {
        //public case:
        JBlock assignmentBlock = new JBlock(false, false);

        assignmentBlock.assign(variable.ref(fieldInjectionPoint.getName()), coerceType(fieldInjectionPoint.getInjectionNode().getASTType(), expression));

        return assignmentBlock;
    }

    private JStatement buildPrivateFieldSet(TypedExpression expression, FieldInjectionPoint fieldInjectionPoint, JExpression variable) throws ClassNotFoundException, JClassAlreadyExistsException {
        return codeModel.ref(InjectionUtil.class).staticInvoke(InjectionUtil.GET_INSTANCE_METHOD).invoke(InjectionUtil.SET_FIELD_METHOD)
                .arg(variable)
                .arg(JExpr.lit(fieldInjectionPoint.getSubclassLevel()))
                .arg(fieldInjectionPoint.getName())
                .arg(expression.getExpression());
    }

    public JExpression buildFieldGet(ASTType returnType, JExpression variable, String name, ASTAccessModifier accessModifier, int subclassLevel) {
        if (accessModifier.equals(ASTAccessModifier.PUBLIC)) {
            return buildPublicFieldGet(variable, name);
        }
        return buildPrivateFieldGet(returnType, variable, name, subclassLevel);
    }

    private JExpression buildPrivateFieldGet(ASTType returnType, JExpression variable, String name, int subclassLevel) {
        return codeModel.ref(InjectionUtil.class).staticInvoke(InjectionUtil.GET_INSTANCE_METHOD).invoke(InjectionUtil.GET_FIELD_METHOD)
                .arg(codeModel.ref(returnType.getName()).dotclass())
                .arg(variable)
                .arg(JExpr.lit(subclassLevel))
                .arg(name);
    }

    private JExpression buildPublicFieldGet(JExpression variable, String name) {
        return variable.ref(name);
    }

    public JExpression buildConstructorCall(Map<InjectionNode, TypedExpression> expressionMap, ConstructorInjectionPoint constructorInjectionPoint, JType type) throws ClassNotFoundException {
        if (constructorInjectionPoint.getAccessModifier().equals(ASTAccessModifier.PUBLIC)) {
            return buildPublicConstructorCall(expressionMap, constructorInjectionPoint, type);
        }
        return buildPrivateConstructorCall(expressionMap, constructorInjectionPoint, type);
    }

    private JExpression buildPublicConstructorCall(Map<InjectionNode, TypedExpression> expressionMap, ConstructorInjectionPoint constructorInjectionPoint, JType type) throws ClassNotFoundException {
        //public clase:
        JInvocation constructorInvocation = JExpr._new(type);

        for (InjectionNode node : constructorInjectionPoint.getInjectionNodes()) {
            constructorInvocation.arg(coerceType(node.getASTType(), expressionMap.get(node)));
        }

        return constructorInvocation;
    }


    private JExpression buildPrivateConstructorCall(Map<InjectionNode, TypedExpression> expressionMap, ConstructorInjectionPoint constructorInjectionPoint, JType type) throws ClassNotFoundException {

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
        JArray argArray = JExpr.newArray(codeModel.ref(Object.class));
        for (InjectionNode injectionNode : constructorInjectionPoint.getInjectionNodes()) {
            argArray.add(expressionMap.get(injectionNode).getExpression());
        }
        constructorInvocation.arg(argArray);

        return constructorInvocation;
    }

    private JExpression coerceType(ASTType targetType, TypedExpression typedExpression) {
        if (targetType.equals(typedExpression.getType())) {
            return typedExpression.getExpression();
        }
        if (targetType.inheritsFrom(typedExpression.getType())) {
            return JExpr.cast(codeModel.ref(targetType.getName()), typedExpression.getExpression());
        }
        if (targetType instanceof ASTPrimitiveType) {
            ASTPrimitiveType primitiveTargetType = (ASTPrimitiveType) targetType;

            ASTType objectType = astClassFactory.buildASTClassType(primitiveTargetType.getObjectClass());

            if (objectType.inheritsFrom(typedExpression.getType())) {
                return JExpr.cast(codeModel.ref(objectType.getName()), typedExpression.getExpression());
            }
        }
        throw new TransfuseAnalysisException("Non-coercible types encountered");
    }
}
