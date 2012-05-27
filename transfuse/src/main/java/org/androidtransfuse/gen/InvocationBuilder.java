package org.androidtransfuse.gen;

import com.sun.codemodel.*;
import org.androidtransfuse.analysis.adapter.ASTAccessModifier;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTParameter;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.ConstructorInjectionPoint;
import org.androidtransfuse.model.FieldInjectionPoint;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.MethodInjectionPoint;
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

    @Inject
    public InvocationBuilder(JCodeModel codeModel) {
        this.codeModel = codeModel;
    }

    public JStatement buildMethodCall(List<ASTParameter> callingParameters, Map<ASTParameter, JExpression> parameters, JExpression targetExpression, ASTMethod methodToCall) throws ClassNotFoundException, JClassAlreadyExistsException {
        List<ASTParameter> matchedParameters = matchMethodArguments(callingParameters, methodToCall);

        if (methodToCall.getAccessModifier().equals(ASTAccessModifier.PUBLIC)) {
            //public access
            return buildPublicMethodCall(parameters, methodToCall.getName(), matchedParameters, targetExpression);
        } else {
            //non-public access
            return buildPrivateMethodCall(0, parameters, methodToCall.getName(), matchedParameters, pullASTParameterTypes(matchedParameters), targetExpression);
        }
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
            JExpression parameter = null;
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

    private <T> JExpression getExpression(Map<T, JExpression> expressionMap, T parameter) {


        if (parameter == null) {
            return JExpr._null();
        } else {
            return expressionMap.get(parameter);
        }
    }

    public JStatement buildMethodCall(Map<InjectionNode, JExpression> nodeMap, MethodInjectionPoint methodInjectionPoint, JExpression variable) throws ClassNotFoundException, JClassAlreadyExistsException {
        if (ASTAccessModifier.PUBLIC.equals(methodInjectionPoint.getAccessModifier())) {
            return buildPublicMethodCall(nodeMap, methodInjectionPoint.getName(), methodInjectionPoint.getInjectionNodes(), variable);
        }
        return buildPrivateMethodCall(methodInjectionPoint.getSuperClassLevel(), nodeMap, methodInjectionPoint.getName(), methodInjectionPoint.getInjectionNodes(), pullASTTypes(methodInjectionPoint.getInjectionNodes()), variable);
    }

    private List<ASTType> pullASTTypes(List<InjectionNode> injectionNodes) {
        List<ASTType> astTypes = new ArrayList<ASTType>();

        for (InjectionNode injectionNode : injectionNodes) {
            astTypes.add(injectionNode.getASTType());
        }

        return astTypes;
    }

    public <T> JStatement buildPrivateMethodCall(int superClassLevel, Map<T, JExpression> nodeMap, String methodName, List<T> injectionNodes, List<ASTType> injectioNodeType, JExpression targetExpression) throws ClassNotFoundException, JClassAlreadyExistsException {

        //InjectionUtil.getInstance().setMethod(Object target, int superLevel, String method, Class[] argClasses,Object[] args)
        JInvocation methodInvocation = codeModel.ref(InjectionUtil.class).staticInvoke(InjectionUtil.GET_INSTANCE_METHOD).invoke(InjectionUtil.CALL_METHOD_METHOD)
                .arg(codeModel.ref(Object.class).dotclass())
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
            argArray.add(getExpression(nodeMap, injectionNode));
        }
        methodInvocation.arg(argArray);

        return methodInvocation;
    }

    public <T> JStatement buildPublicMethodCall(Map<T, JExpression> nodeMap, String methodName, List<T> parameters, JExpression targetExpression) throws ClassNotFoundException, JClassAlreadyExistsException {
        //public case:

        JInvocation methodInvocation = targetExpression.invoke(methodName);

        for (T injectionNode : parameters) {
            methodInvocation.arg(getExpression(nodeMap, injectionNode));
        }

        return methodInvocation;
    }

    public JStatement buildFieldSet(Map<InjectionNode, JExpression> nodeMap, FieldInjectionPoint fieldInjectionPoint, JExpression variable) throws ClassNotFoundException, JClassAlreadyExistsException {
        if (ASTAccessModifier.PUBLIC.equals(fieldInjectionPoint.getAccessModifier())) {
            return buildPublicFieldSet(nodeMap, fieldInjectionPoint, variable);
        }
        return buildPrivateFieldSet(nodeMap, fieldInjectionPoint, variable);
    }

    public JStatement buildPublicFieldSet(Map<InjectionNode, JExpression> nodeMap, FieldInjectionPoint fieldInjectionPoint, JExpression variable) throws ClassNotFoundException, JClassAlreadyExistsException {
        //public case:
        JBlock assignmentBlock = new JBlock(false, false);

        assignmentBlock.assign(variable.ref(fieldInjectionPoint.getName()), nodeMap.get(fieldInjectionPoint.getInjectionNode()));

        return assignmentBlock;
    }

    public JStatement buildPrivateFieldSet(Map<InjectionNode, JExpression> nodeMap, FieldInjectionPoint fieldInjectionPoint, JExpression variable) throws ClassNotFoundException, JClassAlreadyExistsException {
        InjectionNode node = fieldInjectionPoint.getInjectionNode();

        return codeModel.ref(InjectionUtil.class).staticInvoke(InjectionUtil.GET_INSTANCE_METHOD).invoke(InjectionUtil.SET_FIELD_METHOD)
                .arg(variable)
                .arg(JExpr.lit(fieldInjectionPoint.getSubclassLevel()))
                .arg(fieldInjectionPoint.getName())
                .arg(nodeMap.get(node));
    }

    public JInvocation buildConstructorCall(Map<InjectionNode, JExpression> nodeMap, ConstructorInjectionPoint constructorInjectionPoint, JType type) throws ClassNotFoundException {
        if (ASTAccessModifier.PUBLIC.equals(constructorInjectionPoint.getAccessModifier())) {
            return buildPublicConstructorCall(nodeMap, constructorInjectionPoint, type);
        }
        return buildPrivateConstructorCall(nodeMap, constructorInjectionPoint, type);
    }

    public JInvocation buildPublicConstructorCall(Map<InjectionNode, JExpression> nodeMap, ConstructorInjectionPoint constructorInjectionPoint, JType type) throws ClassNotFoundException {
        //public clase:
        JInvocation constructorInvocation = JExpr._new(type);

        for (InjectionNode node : constructorInjectionPoint.getInjectionNodes()) {
            constructorInvocation.arg(nodeMap.get(node));
        }

        return constructorInvocation;
    }


    public JInvocation buildPrivateConstructorCall(Map<InjectionNode, JExpression> nodeMap, ConstructorInjectionPoint constructorInjectionPoint, JType type) throws ClassNotFoundException {

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
            argArray.add(nodeMap.get(injectionNode));
        }
        constructorInvocation.arg(argArray);

        return constructorInvocation;
    }
}
