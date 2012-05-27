package org.androidtransfuse.gen.variableDecorator;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.analysis.astAnalyzer.VirtualProxyAspect;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.InjectionExpressionBuilder;
import org.androidtransfuse.gen.proxy.VirtualProxyGenerator;
import org.androidtransfuse.gen.variableBuilder.ProxyVariableBuilder;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.ProxyDescriptor;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class VirtualProxyExpressionDecorator extends VariableExpressionBuilderDecorator {

    private VirtualProxyGenerator virtualProxyGenerator;
    private ProxyVariableBuilder proxyVariableBuilder;
    @Inject
    private InjectionExpressionBuilder injectionExpressionBuilder;

    @Inject
    public VirtualProxyExpressionDecorator(@Assisted VariableExpressionBuilder decorated,
                                           ProxyVariableBuilder proxyVariableBuilder,
                                           VirtualProxyGenerator virtualProxyGenerator) {
        super(decorated);
        this.proxyVariableBuilder = proxyVariableBuilder;
        this.virtualProxyGenerator = virtualProxyGenerator;
    }

    @Override
    public JExpression buildVariableExpression(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        VirtualProxyAspect proxyAspect = injectionNode.getAspect(VirtualProxyAspect.class);
        Map<InjectionNode, JExpression> variableMap = injectionBuilderContext.getVariableMap();
        JExpression variable;

        if (proxyAspect != null && proxyAspect.isProxyRequired()) {
            //proxy
            ProxyDescriptor proxyDescriptor = virtualProxyGenerator.generateProxy(injectionNode);
            JExpression proxyVariable = proxyVariableBuilder.buildProxyInstance(injectionBuilderContext, injectionNode, proxyDescriptor);
            variableMap.put(injectionNode, proxyVariable);
            //init dependencies
            injectionExpressionBuilder.setupInjectionRequirements(injectionBuilderContext, injectionNode);
            //and initialize delegate
            JExpression delegateVariable = getDecorated().buildVariableExpression(injectionBuilderContext, injectionNode);
            variable = virtualProxyGenerator.initializeProxy(injectionBuilderContext, proxyVariable, delegateVariable);
        } else {
            variable = getDecorated().buildVariableExpression(injectionBuilderContext, injectionNode);
        }

        return variable;
    }
}
