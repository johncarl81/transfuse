package org.androidrobotics.gen;

import com.sun.codemodel.*;
import org.androidrobotics.model.ConstructorInjectionPoint;
import org.androidrobotics.model.FieldInjectionPoint;
import org.androidrobotics.model.InjectionNode;
import org.androidrobotics.model.MethodInjectionPoint;
import org.androidrobotics.util.InjectionUtil;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class InjectionFragmentGenerator {

    private JCodeModel codeModel;
    private VariableNamer variableNamer;

    @Inject
    public InjectionFragmentGenerator(JCodeModel codeModel, VariableNamer variableNamer) {
        this.codeModel = codeModel;
        this.variableNamer = variableNamer;
    }

    public JExpression buildFragment(JBlock block, JDefinedClass definedClass, InjectionNode injectionNode, Map<String, VariableBuilder> variableBuilderMap) throws ClassNotFoundException, JClassAlreadyExistsException {

        Map<InjectionNode, JExpression> nodeVariableMap = new HashMap<InjectionNode, JExpression>();

        JExpression variable = buildVariable(nodeVariableMap, block, definedClass, injectionNode, variableBuilderMap);

        for (Map.Entry<InjectionNode, JExpression> nodeEntry : nodeVariableMap.entrySet()) {

            //field injection
            for (FieldInjectionPoint fieldInjectionPoint : nodeEntry.getKey().getFieldInjectionPoints()) {
                block.add(buildParameterInjection(nodeVariableMap, fieldInjectionPoint, nodeEntry.getValue()));
            }

            //method injection
            for (MethodInjectionPoint methodInjectionPoint : nodeEntry.getKey().getMethodInjectionPoints()) {
                block.add(buildMethodInjection(nodeVariableMap, methodInjectionPoint, nodeEntry.getValue()));
            }
        }

        return variable;
    }

    private JExpression buildVariable(Map<InjectionNode, JExpression> variableMap, JBlock block, JDefinedClass definedClass, InjectionNode injectionNode, Map<String, VariableBuilder> variableBuilderMap) throws ClassNotFoundException, JClassAlreadyExistsException {

        JExpression variable;
        if (variableBuilderMap.containsKey(injectionNode.getClassName())) {
            variable = variableBuilderMap.get(injectionNode.getClassName()).buildVariable();
        } else {
            for (InjectionNode node : injectionNode.getConstructorInjectionPoint().getInjectionNodes()) {
                buildVariable(variableMap, block, definedClass, node, variableBuilderMap);
            }
            //field injection
            for (FieldInjectionPoint fieldInjectionPoint : injectionNode.getFieldInjectionPoints()) {
                buildVariable(variableMap, block, definedClass, fieldInjectionPoint.getInjectionNode(), variableBuilderMap);
            }
            //method injection
            for (MethodInjectionPoint methodInjectionPoint : injectionNode.getMethodInjectionPoints()) {
                for (InjectionNode node : methodInjectionPoint.getInjectionNodes()) {
                    buildVariable(variableMap, block, definedClass, node, variableBuilderMap);
                }
            }

            JType nodeType = codeModel.parseType(injectionNode.getClassName());
 
            JVar variableRef = definedClass.field(JMod.PRIVATE, nodeType, variableNamer.generateName(injectionNode.getClassName()));
            variable = variableRef;

            //constructor injection
            block.assign(variableRef, buildConstructorCall(variableMap, injectionNode.getConstructorInjectionPoint(), nodeType));
        }

        variableMap.put(injectionNode, variable);

        return variable;
    }

    private JInvocation buildMethodInjection(Map<InjectionNode, JExpression> nodeMap, MethodInjectionPoint methodInjectionPoint, JExpression variable) throws ClassNotFoundException, JClassAlreadyExistsException {
        JInvocation methodInvocation = variable.invoke(methodInjectionPoint.getName());

        for (InjectionNode injectionNode : methodInjectionPoint.getInjectionNodes()) {
            methodInvocation.arg(nodeMap.get(injectionNode));
        }

        return methodInvocation;
    }

    private JInvocation buildParameterInjection(Map<InjectionNode, JExpression> nodeMap, FieldInjectionPoint fieldInjectionPoint, JExpression variable) throws ClassNotFoundException, JClassAlreadyExistsException {
        InjectionNode node = fieldInjectionPoint.getInjectionNode();

        return codeModel.ref(InjectionUtil.class).staticInvoke(InjectionUtil.SET_FIELD_METHOD)
                .arg(variable).arg(fieldInjectionPoint.getName()).arg(nodeMap.get(node));
    }

    private JInvocation buildConstructorCall(Map<InjectionNode, JExpression> nodeMap, ConstructorInjectionPoint injectionNode, JType type) throws ClassNotFoundException, JClassAlreadyExistsException {
        JInvocation constructorInvocation = JExpr._new(type);

        for (InjectionNode node : injectionNode.getInjectionNodes()) {
            constructorInvocation.arg(nodeMap.get(node));
        }

        return constructorInvocation;
    }
}
