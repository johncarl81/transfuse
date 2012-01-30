package org.androidrobotics.gen;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpression;
import org.androidrobotics.analysis.astAnalyzer.ASTInjectionAspect;
import org.androidrobotics.analysis.astAnalyzer.VirtualProxyAspect;
import org.androidrobotics.gen.proxy.VirtualProxyGenerator;
import org.androidrobotics.gen.variableBuilder.ProxyVariableBuilder;
import org.androidrobotics.gen.variableBuilder.VariableBuilder;
import org.androidrobotics.model.FieldInjectionPoint;
import org.androidrobotics.model.InjectionNode;
import org.androidrobotics.model.MethodInjectionPoint;
import org.androidrobotics.model.ProxyDescriptor;
import org.androidrobotics.model.r.RResource;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class InjectionBuilderContext {

    private Map<InjectionNode, JExpression> variableMap;
    private JBlock block;
    private JDefinedClass definedClass;
    private VirtualProxyGenerator virtualProxyGenerator;
    private ProxyVariableBuilder proxyVariableBuilder;
    private RResource rResource;

    @Inject
    public InjectionBuilderContext(@Assisted Map<InjectionNode, JExpression> variableMap,
                                   @Assisted JBlock block,
                                   @Assisted JDefinedClass definedClass,
                                   @Assisted RResource rResource,
                                   ProxyVariableBuilder proxyVariableBuilder,
                                   VirtualProxyGenerator virtualProxyGenerator) {
        this.variableMap = variableMap;
        this.block = block;
        this.definedClass = definedClass;
        this.proxyVariableBuilder = proxyVariableBuilder;
        this.virtualProxyGenerator = virtualProxyGenerator;
        this.rResource = rResource;
    }

    public JExpression buildVariable(InjectionNode injectionNode) {

        JExpression variable;

        if (variableMap.containsKey(injectionNode)) {
            variable = variableMap.get(injectionNode);
        } else {
            VirtualProxyAspect proxyAspect = injectionNode.getAspect(VirtualProxyAspect.class);
            if (proxyAspect != null && proxyAspect.isProxyRequired()) {
                //proxy
                ProxyDescriptor proxyDescriptor = virtualProxyGenerator.generateProxy(injectionNode);
                JExpression proxyVariable = proxyVariableBuilder.buildProxyInstance(this, injectionNode, proxyDescriptor);
                variableMap.put(injectionNode, proxyVariable);
                //init dependencies
                setupInjectionRequirements(injectionNode);
                //and initialize delegate
                JExpression delegateVariable = executeVariableBuilder(injectionNode);
                variable = virtualProxyGenerator.initalizeProxy(this, proxyVariable, delegateVariable);

                variableMap.put(injectionNode, delegateVariable);
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

    public RResource getRResource() {
        return rResource;
    }
}
