package org.androidrobotics.gen;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpression;
import org.androidrobotics.model.FieldInjectionPoint;
import org.androidrobotics.model.InjectionNode;
import org.androidrobotics.model.MethodInjectionPoint;
import org.androidrobotics.model.ProxyDescriptor;

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
    private ProxyGenerator proxyGenerator;
    private DelegateInstantiationGeneratorStrategyFactory proxyStrategyFactory;
    private ProxyVariableBuilder proxyVariableBuilder;

    public InjectionBuilderContext(Map<InjectionNode, JExpression> variableMap, JBlock block, JDefinedClass definedClass, InjectionNode injectionNode, VariableBuilderRepository variableBuilderMap, DelegateInstantiationGeneratorStrategyFactory proxyStrategyFactory, ProxyVariableBuilder proxyVariableBuilder, ProxyGenerator proxyGenerator) {
        this.variableMap = variableMap;
        this.block = block;
        this.definedClass = definedClass;
        this.injectionNode = injectionNode;
        this.variableBuilderMap = variableBuilderMap;
        this.proxyStrategyFactory = proxyStrategyFactory;
        this.proxyVariableBuilder = proxyVariableBuilder;
        this.proxyGenerator = proxyGenerator;
    }

    public JExpression buildVariable() {

        JExpression variable;

        if (variableMap.containsKey(injectionNode)) {
            variable = variableMap.get(injectionNode);
        } else {
            if (injectionNode.isProxyRequired()) {
                DelegateDelayedGeneratorStrategy delayedGeneratorStrategy = proxyStrategyFactory.buildDelayedStrategy(injectionNode);
                //proxy
                ProxyDescriptor proxyDescriptor = proxyGenerator.generateProxy(injectionNode, delayedGeneratorStrategy);
                JExpression proxyVariable = proxyVariableBuilder.buildProxyInstance(this, proxyDescriptor);
                variableMap.put(injectionNode, proxyVariable);
                //then init dependencies
                setupInjectionRequirements();
                //and initialize
                variable = delayedGeneratorStrategy.initalizeProxy(this, proxyVariable, executeVariableBuilder());
                variableMap.put(injectionNode, variable);
            } else {
                variable = executeVariableBuilder();
                variableMap.put(injectionNode, variable);
            }
        }

        return variable;
    }

    private JExpression executeVariableBuilder() {
        VariableBuilder variableBuilder = variableBuilderMap.get(injectionNode.getClassName());
        return variableBuilder.buildVariable(this);
    }

    public void setupInjectionRequirements() {
        //constructor injection
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
        return new InjectionBuilderContext(variableMap, block, definedClass, node, variableBuilderMap, proxyStrategyFactory, proxyVariableBuilder, proxyGenerator);
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
