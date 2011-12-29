package org.androidrobotics.gen;

import com.sun.codemodel.*;
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

    private JCodeModel codeModel;

    @Inject
    public InjectionInvocationBuilder(JCodeModel codeModel) {
        this.codeModel = codeModel;
    }

    public JInvocation buildMethodInjection(Map<InjectionNode, JExpression> nodeMap, MethodInjectionPoint methodInjectionPoint, JExpression variable) throws ClassNotFoundException, JClassAlreadyExistsException {
        JInvocation methodInvocation = variable.invoke(methodInjectionPoint.getName());

        for (InjectionNode injectionNode : methodInjectionPoint.getInjectionNodes()) {
            methodInvocation.arg(nodeMap.get(injectionNode));
        }

        return methodInvocation;
    }

    public JInvocation buildParameterInjection(Map<InjectionNode, JExpression> nodeMap, FieldInjectionPoint fieldInjectionPoint, JExpression variable) throws ClassNotFoundException, JClassAlreadyExistsException {
        InjectionNode node = fieldInjectionPoint.getInjectionNode();

        return codeModel.ref(InjectionUtil.class).staticInvoke(InjectionUtil.SET_FIELD_METHOD)
                .arg(variable).arg(fieldInjectionPoint.getName())
                .arg(nodeMap.get(node))
                .arg(JExpr.lit(fieldInjectionPoint.isProxied()));
    }

    public JInvocation buildConstructorCall(Map<InjectionNode, JExpression> nodeMap, ConstructorInjectionPoint injectionNode, JType type) throws ClassNotFoundException {
        JInvocation constructorInvocation = JExpr._new(type);

        for (InjectionNode node : injectionNode.getInjectionNodes()) {
            constructorInvocation.arg(nodeMap.get(node));
        }

        return constructorInvocation;
    }

}
