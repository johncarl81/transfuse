package org.androidrobotics.gen;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpression;
import org.androidrobotics.model.FieldInjectionPoint;
import org.androidrobotics.model.InjectionNode;
import org.androidrobotics.model.MethodInjectionPoint;

import java.util.Map;

/**
 * @author John Ericksen
 */
public class InjectionBuilderContext {

    private Map<InjectionNode, JExpression> variableMap;
    private JBlock block;
    private JDefinedClass definedClass;
    private InjectionNode injectionNode;
    private VariableBuilderRepository variableBuilderMap;

    public InjectionBuilderContext(Map<InjectionNode, JExpression> variableMap, JBlock block, JDefinedClass definedClass, InjectionNode injectionNode, VariableBuilderRepository variableBuilderMap) {
        this.variableMap = variableMap;
        this.block = block;
        this.definedClass = definedClass;
        this.injectionNode = injectionNode;
        this.variableBuilderMap = variableBuilderMap;
    }

    public JExpression buildVariable() {

        VariableBuilder variableBuilder = variableBuilderMap.get(injectionNode.getClassName());

        JExpression variable = variableBuilder.buildVariable(this);
        variableMap.put(injectionNode, variable);

        return variable;
    }

    public void setupInjectionRequirements() {
        for (InjectionNode node : injectionNode.getConstructorInjectionPoint().getInjectionNodes()) {
            buildNextContext(node).buildVariable();
        }
        //field injection
        for (FieldInjectionPoint fieldInjectionPoint : injectionNode.getFieldInjectionPoints()) {
            buildNextContext(fieldInjectionPoint.getInjectionNode()).buildVariable();
        }
        //method injection
        for (MethodInjectionPoint methodInjectionPoint : injectionNode.getMethodInjectionPoints()) {
            for (InjectionNode node : methodInjectionPoint.getInjectionNodes()) {
                buildNextContext(node).buildVariable();
            }
        }
    }

    public InjectionBuilderContext buildNextContext(InjectionNode node) {
        return new InjectionBuilderContext(variableMap, block, definedClass, node, variableBuilderMap);
    }

    public Map<InjectionNode, JExpression> getVariableMap() {
        return variableMap;
    }

    public JBlock getBlock() {
        return block;
    }

    public JDefinedClass getDefinedClass() {
        return definedClass;
    }

    public InjectionNode getInjectionNode() {
        return injectionNode;
    }
}
