package org.androidtransfuse.gen.variableDecorator;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.analysis.astAnalyzer.VirtualProxyAspect;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.InjectionExpressionBuilder;
import org.androidtransfuse.gen.proxy.VirtualProxyGenerator;
import org.androidtransfuse.gen.variableBuilder.ProxyVariableBuilder;
import org.androidtransfuse.gen.variableBuilder.TypedExpressionFactory;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.ProxyDescriptor;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class VirtualProxyExpressionDecorator extends VariableExpressionBuilderDecorator {

    private VirtualProxyGenerator virtualProxyGenerator;
    private ProxyVariableBuilder proxyVariableBuilder;
    private InjectionExpressionBuilder injectionExpressionBuilder;
    private TypedExpressionFactory typedExpressionFactory;

    @Inject
    public VirtualProxyExpressionDecorator(@Assisted VariableExpressionBuilder decorated,
                                           ProxyVariableBuilder proxyVariableBuilder,
                                           VirtualProxyGenerator virtualProxyGenerator,
                                           InjectionExpressionBuilder injectionExpressionBuilder,
                                           TypedExpressionFactory typedExpressionFactory) {
        super(decorated);
        this.proxyVariableBuilder = proxyVariableBuilder;
        this.virtualProxyGenerator = virtualProxyGenerator;
        this.injectionExpressionBuilder = injectionExpressionBuilder;
        this.typedExpressionFactory = typedExpressionFactory;
    }

    @Override
    public TypedExpression buildVariableExpression(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        VirtualProxyAspect proxyAspect = injectionNode.getAspect(VirtualProxyAspect.class);
        Map<InjectionNode, TypedExpression> variableMap = injectionBuilderContext.getVariableMap();
        TypedExpression variable;

        if (proxyAspect != null && proxyAspect.isProxyRequired()) {
            //proxy
            ProxyDescriptor proxyDescriptor = virtualProxyGenerator.generateProxy(injectionNode);
            JExpression proxyExpression = proxyVariableBuilder.buildProxyInstance(injectionBuilderContext, injectionNode, proxyDescriptor);
            TypedExpression proxyVariable = typedExpressionFactory.build(injectionNode.getASTType(), proxyExpression);
            variableMap.put(injectionNode, proxyVariable);
            //init dependencies
            injectionExpressionBuilder.setupInjectionRequirements(injectionBuilderContext, injectionNode);
            //and initialize delegate
            TypedExpression delegateVariable = getDecorated().buildVariableExpression(injectionBuilderContext, injectionNode);
            variable = virtualProxyGenerator.initializeProxy(injectionBuilderContext, proxyVariable, delegateVariable);
        } else {
            variable = getDecorated().buildVariableExpression(injectionBuilderContext, injectionNode);
        }

        return variable;
    }
}
