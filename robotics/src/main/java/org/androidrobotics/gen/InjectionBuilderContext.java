package org.androidrobotics.gen;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpression;
import org.androidrobotics.analysis.astAnalyzer.ASTInjectionAspect;
import org.androidrobotics.analysis.astAnalyzer.ProxyAspect;
import org.androidrobotics.gen.proxy.DelegateDelayedGeneratorStrategy;
import org.androidrobotics.gen.proxy.ProxyGenerator;
import org.androidrobotics.gen.variableBuilder.ProxyVariableBuilder;
import org.androidrobotics.gen.variableBuilder.VariableBuilder;
import org.androidrobotics.model.FieldInjectionPoint;
import org.androidrobotics.model.InjectionNode;
import org.androidrobotics.model.MethodInjectionPoint;
import org.androidrobotics.model.ProxyDescriptor;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class InjectionBuilderContext {

    private Map<InjectionNode, JExpression> variableMap;
    private JBlock block;
    private JDefinedClass definedClass;
    private ProxyGenerator proxyGenerator;
    private DelegateDelayedGeneratorStrategy delayedGeneratorStrategy;
    private ProxyVariableBuilder proxyVariableBuilder;

    @Inject
    public InjectionBuilderContext(@Assisted Map<InjectionNode, JExpression> variableMap,
                                   @Assisted JBlock block,
                                   @Assisted JDefinedClass definedClass,
                                   ProxyVariableBuilder proxyVariableBuilder,
                                   ProxyGenerator proxyGenerator,
                                   DelegateDelayedGeneratorStrategy delayedGeneratorStrategy) {
        this.variableMap = variableMap;
        this.block = block;
        this.definedClass = definedClass;
        this.proxyVariableBuilder = proxyVariableBuilder;
        this.proxyGenerator = proxyGenerator;
        this.delayedGeneratorStrategy = delayedGeneratorStrategy;
    }

    public JExpression buildVariable(InjectionNode injectionNode) {

        JExpression variable;

        if (variableMap.containsKey(injectionNode)) {
            variable = variableMap.get(injectionNode);
        } else {
            ProxyAspect proxyAspect = injectionNode.getAspect(ProxyAspect.class);
            if (proxyAspect != null && proxyAspect.isProxyRequired()) {
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
        VariableBuilder variableBuilder = injectionNode.getAspect(VariableBuilder.class);
        return variableBuilder.buildVariable(this, injectionNode);
    }

    public void setupInjectionRequirements(InjectionNode injectionNode) {
        ASTInjectionAspect injectionAspect = injectionNode.getAspect(ASTInjectionAspect.class);
        if (injectionAspect != null) {
            //constructor injection
            for (InjectionNode constructorNode : injectionAspect.getConstructorInjectionPoint().getInjectionNodes()) {
                buildVariable(constructorNode);
            }
            //field injection
            for (FieldInjectionPoint fieldInjectionPoint : injectionAspect.getFieldInjectionPoints()) {
                buildVariable(fieldInjectionPoint.getInjectionNode());
            }
            //method injection
            for (MethodInjectionPoint methodInjectionPoint : injectionAspect.getMethodInjectionPoints()) {
                for (InjectionNode methodNode : methodInjectionPoint.getInjectionNodes()) {
                    buildVariable(methodNode);
                }
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
