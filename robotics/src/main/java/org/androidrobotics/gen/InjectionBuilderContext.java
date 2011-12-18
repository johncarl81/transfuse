package org.androidrobotics.gen;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpression;
import org.androidrobotics.gen.proxy.DelegateDelayedGeneratorStrategy;
import org.androidrobotics.gen.proxy.DelegateInstantiationGeneratorStrategyFactory;
import org.androidrobotics.gen.proxy.ProxyGenerator;
import org.androidrobotics.gen.variableBuilder.ProxyVariableBuilder;
import org.androidrobotics.gen.variableBuilder.VariableBuilder;
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
    private VariableBuilderRepository variableBuilderMap;
    private ProxyGenerator proxyGenerator;
    private DelegateInstantiationGeneratorStrategyFactory proxyStrategyFactory;
    private ProxyVariableBuilder proxyVariableBuilder;

    public InjectionBuilderContext(Map<InjectionNode, JExpression> variableMap,
                                   JBlock block,
                                   JDefinedClass definedClass,
                                   VariableBuilderRepository variableBuilderMap,
                                   DelegateInstantiationGeneratorStrategyFactory proxyStrategyFactory,
                                   ProxyVariableBuilder proxyVariableBuilder,
                                   ProxyGenerator proxyGenerator) {
        this.variableMap = variableMap;
        this.block = block;
        this.definedClass = definedClass;
        this.variableBuilderMap = variableBuilderMap;
        this.proxyStrategyFactory = proxyStrategyFactory;
        this.proxyVariableBuilder = proxyVariableBuilder;
        this.proxyGenerator = proxyGenerator;
    }

    public JExpression buildVariable(InjectionNode injectionNode) {

        JExpression variable;

        if (variableMap.containsKey(injectionNode)) {
            variable = variableMap.get(injectionNode);
        } else {
            if (injectionNode.isProxyRequired()) {
                DelegateDelayedGeneratorStrategy delayedGeneratorStrategy = proxyStrategyFactory.buildDelayedStrategy(injectionNode);
                //proxy
                ProxyDescriptor proxyDescriptor = proxyGenerator.generateProxy(injectionNode, delayedGeneratorStrategy);
                JExpression proxyVariable = proxyVariableBuilder.buildProxyInstance(this, injectionNode, proxyDescriptor);
                variableMap.put(injectionNode, proxyVariable);
                //then init dependencies
                setupInjectionRequirements(injectionNode);
                //and initialize
                variable = delayedGeneratorStrategy.initalizeProxy(this, proxyVariable, executeVariableBuilder(injectionNode));
                variableMap.put(injectionNode, variable);
            } else {
                variable = executeVariableBuilder(injectionNode);
                variableMap.put(injectionNode, variable);
            }
        }

        return variable;
    }

    private JExpression executeVariableBuilder(InjectionNode injectionNode) {
        VariableBuilder variableBuilder = variableBuilderMap.get(injectionNode.getClassName());
        return variableBuilder.buildVariable(this, injectionNode);
    }

    public void setupInjectionRequirements(InjectionNode injectionNode) {
        //constructor injection
        for (InjectionNode constructorNode : injectionNode.getConstructorInjectionPoint().getInjectionNodes()) {
            buildVariable(constructorNode);
        }
        //field injection
        for (FieldInjectionPoint fieldInjectionPoint : injectionNode.getFieldInjectionPoints()) {
            buildVariable(fieldInjectionPoint.getInjectionNode());
        }
        //method injection
        for (MethodInjectionPoint methodInjectionPoint : injectionNode.getMethodInjectionPoints()) {
            for (InjectionNode methodNode : methodInjectionPoint.getInjectionNodes()) {
                buildVariable(methodNode);
            }
        }
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
}
