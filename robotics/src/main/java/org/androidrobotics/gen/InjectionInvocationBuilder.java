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

    public JInvocation buildFieldInjection(Map<InjectionNode, JExpression> nodeMap, FieldInjectionPoint fieldInjectionPoint, JExpression variable) throws ClassNotFoundException, JClassAlreadyExistsException {
        if (fieldInjectionPoint.isProxied()) {
            return buildFieldInjection(InjectionUtil.SET_SUPER_METHOD, nodeMap, fieldInjectionPoint, variable);
        } else {
            return buildFieldInjection(InjectionUtil.SET_FIELD_METHOD, nodeMap, fieldInjectionPoint, variable);
        }
    }

    private JInvocation buildFieldInjection(String staticMethod, Map<InjectionNode, JExpression> nodeMap, FieldInjectionPoint fieldInjectionPoint, JExpression variable) throws ClassNotFoundException, JClassAlreadyExistsException {
        InjectionNode node = fieldInjectionPoint.getInjectionNode();

        return codeModel.ref(InjectionUtil.class).staticInvoke(staticMethod)
                .arg(variable).arg(fieldInjectionPoint.getName())
                .arg(nodeMap.get(node));
    }

    public JInvocation buildConstructorCall(Map<InjectionNode, JExpression> nodeMap, ConstructorInjectionPoint injectionNode, JType type) throws ClassNotFoundException {
        JInvocation constructorInvocation = JExpr._new(type);

        for (InjectionNode node : injectionNode.getInjectionNodes()) {
            constructorInvocation.arg(nodeMap.get(node));
        }

        return constructorInvocation;
    }
}
