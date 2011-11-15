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

    public JVar buildFragment(JBlock block, JDefinedClass definedClass, InjectionNode injectionNode) throws ClassNotFoundException, JClassAlreadyExistsException {

        Map<InjectionNode, JVar> nodeVariableMap = new HashMap<InjectionNode, JVar>();

        JVar variable = buildConstructorCall(nodeVariableMap, block, definedClass, injectionNode);

        for (Map.Entry<InjectionNode, JVar> nodeEntry : nodeVariableMap.entrySet()) {

            //field injection
            for (FieldInjectionPoint fieldInjectionPoint : nodeEntry.getKey().getFieldInjectionPoints()) {
                buildParameterInjection(nodeVariableMap, fieldInjectionPoint, nodeEntry.getValue(), block, definedClass);
            }

            //method injection
            for (MethodInjectionPoint methodInjectionPoint : nodeEntry.getKey().getMethodInjectionPoints()) {
                buildMethodInjection(nodeVariableMap, methodInjectionPoint, nodeEntry.getValue(), block, definedClass);
            }
        }

        return variable;
    }

    private JVar buildConstructorCall(Map<InjectionNode, JVar> nodeMap, JBlock block, JDefinedClass definedClass, InjectionNode injectionNode) throws ClassNotFoundException, JClassAlreadyExistsException {


        for (InjectionNode node : injectionNode.getConstructorInjectionPoint().getInjectionNodes()) {
            buildConstructorCall(nodeMap, block, definedClass, node);
        }
        //field injection
        for (FieldInjectionPoint fieldInjectionPoint : injectionNode.getFieldInjectionPoints()) {
            buildConstructorCall(nodeMap, block, definedClass, fieldInjectionPoint.getInjectionNode());
        }
        //method injection
        for (MethodInjectionPoint methodInjectionPoint : injectionNode.getMethodInjectionPoints()) {
            for (InjectionNode node : methodInjectionPoint.getInjectionNodes()) {
                buildConstructorCall(nodeMap, block, definedClass, node);
            }
        }

        JType nodeType = codeModel.parseType(injectionNode.getClassName());

        JVar variable = definedClass.field(JMod.PRIVATE, nodeType, variableNamer.generateName(injectionNode.getClassName()));

        //constructor injection
        block.assign(variable, buildConstructorCall(nodeMap, injectionNode.getConstructorInjectionPoint(), nodeType));

        nodeMap.put(injectionNode, variable);

        return variable;
    }

    private void buildMethodInjection(Map<InjectionNode, JVar> nodeMap, MethodInjectionPoint methodInjectionPoint, JVar variable, JBlock body, JDefinedClass definedClass) throws ClassNotFoundException, JClassAlreadyExistsException {
        JInvocation methodInvocation = variable.invoke(methodInjectionPoint.getName());

        for (InjectionNode injectionNode : methodInjectionPoint.getInjectionNodes()) {
            methodInvocation.arg(nodeMap.get(injectionNode));
        }

        body.add(methodInvocation);

    }

    private void buildParameterInjection(Map<InjectionNode, JVar> nodeMap, FieldInjectionPoint fieldInjectionPoint, JVar variable, JBlock body, JDefinedClass definedClass) throws ClassNotFoundException, JClassAlreadyExistsException {
        InjectionNode node = fieldInjectionPoint.getInjectionNode();

        body.add(codeModel.ref(InjectionUtil.class).staticInvoke(InjectionUtil.SET_FIELD_METHOD)
                .arg(variable).arg(fieldInjectionPoint.getName()).arg(nodeMap.get(node)));
    }

    private JInvocation buildConstructorCall(Map<InjectionNode, JVar> nodeMap, ConstructorInjectionPoint injectionNode, JType type) throws ClassNotFoundException, JClassAlreadyExistsException {
        JInvocation constructorInvocation = JExpr._new(type);

        for (InjectionNode node : injectionNode.getInjectionNodes()) {
            constructorInvocation.arg(nodeMap.get(node));
        }

        return constructorInvocation;
    }
}
