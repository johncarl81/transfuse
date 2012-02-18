package org.androidtransfuse.gen;

import com.sun.codemodel.JExpression;
import org.androidtransfuse.analysis.astAnalyzer.ASTInjectionAspect;
import org.androidtransfuse.analysis.astAnalyzer.VirtualProxyAspect;
import org.androidtransfuse.gen.proxy.VirtualProxyGenerator;
import org.androidtransfuse.gen.variableBuilder.ProxyVariableBuilder;
import org.androidtransfuse.gen.variableBuilder.VariableBuilder;
import org.androidtransfuse.model.FieldInjectionPoint;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.MethodInjectionPoint;
import org.androidtransfuse.model.ProxyDescriptor;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class InjectionVariableBuilder {

    private VirtualProxyGenerator virtualProxyGenerator;
    private ProxyVariableBuilder proxyVariableBuilder;

    @Inject
    public InjectionVariableBuilder(VirtualProxyGenerator virtualProxyGenerator, ProxyVariableBuilder proxyVariableBuilder) {
        this.virtualProxyGenerator = virtualProxyGenerator;
        this.proxyVariableBuilder = proxyVariableBuilder;
    }

    public JExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        JExpression variable;

        Map<InjectionNode,JExpression> variableMap = injectionBuilderContext.getVariableMap();
        if (variableMap.containsKey(injectionNode)) {
            variable = variableMap.get(injectionNode);
        } else {
            VirtualProxyAspect proxyAspect = injectionNode.getAspect(VirtualProxyAspect.class);
            if (proxyAspect != null && proxyAspect.isProxyRequired()) {
                //proxy
                ProxyDescriptor proxyDescriptor = virtualProxyGenerator.generateProxy(injectionNode);
                JExpression proxyVariable = proxyVariableBuilder.buildProxyInstance(injectionBuilderContext, injectionNode, proxyDescriptor);
                variableMap.put(injectionNode, proxyVariable);
                //init dependencies
                setupInjectionRequirements(injectionBuilderContext, injectionNode);
                //and initialize delegate
                JExpression delegateVariable = executeVariableBuilder(injectionBuilderContext, injectionNode);
                variable = virtualProxyGenerator.initalizeProxy(injectionBuilderContext, proxyVariable, delegateVariable);

                variableMap.put(injectionNode, delegateVariable);
            } else {
                variable = executeVariableBuilder(injectionBuilderContext, injectionNode);
                variableMap.put(injectionNode, variable);
            }
        }

        return variable;
    }

    private JExpression executeVariableBuilder(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        VariableBuilder variableBuilder = injectionNode.getAspect(VariableBuilder.class);
        return variableBuilder.buildVariable(injectionBuilderContext, injectionNode);
    }

    public void setupInjectionRequirements(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        ASTInjectionAspect injectionAspect = injectionNode.getAspect(ASTInjectionAspect.class);
        if (injectionAspect != null) {
            //constructor injection
            for (InjectionNode constructorNode : injectionAspect.getConstructorInjectionPoint().getInjectionNodes()) {
                buildVariable(injectionBuilderContext, constructorNode);
            }
            //field injection
            for (FieldInjectionPoint fieldInjectionPoint : injectionAspect.getFieldInjectionPoints()) {
                buildVariable(injectionBuilderContext, fieldInjectionPoint.getInjectionNode());
            }
            //method injection
            for (MethodInjectionPoint methodInjectionPoint : injectionAspect.getMethodInjectionPoints()) {
                for (InjectionNode methodNode : methodInjectionPoint.getInjectionNodes()) {
                    buildVariable(injectionBuilderContext, methodNode);
                }
            }
        }
    }
}
