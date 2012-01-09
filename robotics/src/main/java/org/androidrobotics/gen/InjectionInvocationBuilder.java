package org.androidrobotics.gen;

import com.sun.codemodel.*;
import org.androidrobotics.analysis.adapter.ASTAccessModifier;
import org.androidrobotics.model.ConstructorInjectionPoint;
import org.androidrobotics.model.FieldInjectionPoint;
import org.androidrobotics.model.InjectionNode;
import org.androidrobotics.model.MethodInjectionPoint;
import org.androidrobotics.util.InjectionUtil;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class InjectionInvocationBuilder {

    private static final String CLASS_REF = "class";

    private JCodeModel codeModel;

    @Inject
    public InjectionInvocationBuilder(JCodeModel codeModel) {
        this.codeModel = codeModel;
    }

    public JStatement buildMethodInjection(Map<InjectionNode, JExpression> nodeMap, MethodInjectionPoint methodInjectionPoint, JExpression variable) throws ClassNotFoundException, JClassAlreadyExistsException {
        if (ASTAccessModifier.PUBLIC.equals(methodInjectionPoint.getAccessModifier())) {
            return buildPublicMethodInjection(nodeMap, methodInjectionPoint, variable);
        }
        return buildPrivateMethodInjection(nodeMap, methodInjectionPoint, variable);
    }

    public JStatement buildPrivateMethodInjection(Map<InjectionNode, JExpression> nodeMap, MethodInjectionPoint methodInjectionPoint, JExpression variable) throws ClassNotFoundException, JClassAlreadyExistsException {

        //InjectionUtil.setMethod(Object target, int superLevel, String method, Class[] argClasses,Object[] args)
        JInvocation methodInvocation = codeModel.ref(InjectionUtil.class).staticInvoke(InjectionUtil.SET_METHOD_METHOD)
                .arg(variable)
                .arg(JExpr.lit(0))
                .arg(methodInjectionPoint.getName());

        //add classes
        JArray classArray = JExpr.newArray(codeModel.ref(Class.class));
        for (InjectionNode injectionNode : methodInjectionPoint.getInjectionNodes()) {
            classArray.add(codeModel.ref(injectionNode.getClassName()).staticRef(CLASS_REF));
        }
        methodInvocation.arg(classArray);

        //add args
        JArray argArray = JExpr.newArray(codeModel.ref(Object.class));
        for (InjectionNode injectionNode : methodInjectionPoint.getInjectionNodes()) {
            argArray.add(nodeMap.get(injectionNode));
        }
        methodInvocation.arg(argArray);

        return methodInvocation;
    }

    public JStatement buildPublicMethodInjection(Map<InjectionNode, JExpression> nodeMap, MethodInjectionPoint methodInjectionPoint, JExpression variable) throws ClassNotFoundException, JClassAlreadyExistsException {
        //public case:

        JInvocation methodInvocation = variable.invoke(methodInjectionPoint.getName());

        for (InjectionNode injectionNode : methodInjectionPoint.getInjectionNodes()) {
            methodInvocation.arg(nodeMap.get(injectionNode));
        }

        return methodInvocation;
    }

    public JStatement buildFieldInjection(Map<InjectionNode, JExpression> nodeMap, FieldInjectionPoint fieldInjectionPoint, JExpression variable) throws ClassNotFoundException, JClassAlreadyExistsException {
        if (ASTAccessModifier.PUBLIC.equals(fieldInjectionPoint.getAccessModifier())) {
            return buildPublicFieldInjection(nodeMap, fieldInjectionPoint, variable);
        }
        return buildPrivateFieldInjection(nodeMap, fieldInjectionPoint, variable);
    }

    public JStatement buildPrivateFieldInjection(Map<InjectionNode, JExpression> nodeMap, FieldInjectionPoint fieldInjectionPoint, JExpression variable) throws ClassNotFoundException, JClassAlreadyExistsException {
        if (fieldInjectionPoint.isProxied()) {
            return buildFieldInjection(1, nodeMap, fieldInjectionPoint, variable);
        } else {
            return buildFieldInjection(0, nodeMap, fieldInjectionPoint, variable);
        }
    }

    public JStatement buildPublicFieldInjection(Map<InjectionNode, JExpression> nodeMap, FieldInjectionPoint fieldInjectionPoint, JExpression variable) throws ClassNotFoundException, JClassAlreadyExistsException {
        //public case:
        JBlock assignmentBlock = new JBlock(false, false);

        assignmentBlock.assign(variable.ref(fieldInjectionPoint.getName()), nodeMap.get(fieldInjectionPoint.getInjectionNode()));

        return assignmentBlock;
    }

    private JStatement buildFieldInjection(int superLevel, Map<InjectionNode, JExpression> nodeMap, FieldInjectionPoint fieldInjectionPoint, JExpression variable) throws ClassNotFoundException, JClassAlreadyExistsException {
        InjectionNode node = fieldInjectionPoint.getInjectionNode();

        return codeModel.ref(InjectionUtil.class).staticInvoke(InjectionUtil.SET_FIELD_METHOD)
                .arg(variable)
                .arg(JExpr.lit(superLevel))
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
        JInvocation constructorInvocation = codeModel.ref(InjectionUtil.class).staticInvoke(InjectionUtil.SET_CONSTRUCTOR_METHOD)
                .arg(codeModel.ref(type.fullName()).staticRef(CLASS_REF));

        //add classes
        JArray classArray = JExpr.newArray(codeModel.ref(Class.class));
        for (InjectionNode injectionNode : constructorInjectionPoint.getInjectionNodes()) {
            classArray.add(codeModel.ref(injectionNode.getUsageType().getName()).staticRef(CLASS_REF));
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
