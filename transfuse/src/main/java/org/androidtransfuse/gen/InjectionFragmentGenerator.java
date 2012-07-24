package org.androidtransfuse.gen;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.analysis.astAnalyzer.VirtualProxyAspect;
import org.androidtransfuse.gen.proxy.VirtualProxyGenerator;
import org.androidtransfuse.gen.variableDecorator.LateInit;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author John Ericksen
 */
public class InjectionFragmentGenerator {

    private InjectionBuilderContextFactory injectionBuilderContextFactory;
    private InjectionExpressionBuilder injectionExpressionBuilder;
    private VirtualProxyGenerator virtualProxyGenerator;

    @Inject
    public InjectionFragmentGenerator(InjectionBuilderContextFactory injectionBuilderContextFactory, InjectionExpressionBuilder injectionExpressionBuilder, VirtualProxyGenerator virtualProxyGenerator) {
        this.injectionBuilderContextFactory = injectionBuilderContextFactory;
        this.injectionExpressionBuilder = injectionExpressionBuilder;
        this.virtualProxyGenerator = virtualProxyGenerator;
    }

    public Map<InjectionNode, TypedExpression> buildFragment(JBlock block, JDefinedClass definedClass, InjectionNode injectionNode) throws ClassNotFoundException, JClassAlreadyExistsException {

        Map<InjectionNode, TypedExpression> nodeVariableMap = new HashMap<InjectionNode, TypedExpression>();
        InjectionBuilderContext injectionBuilderContext = injectionBuilderContextFactory.buildContext(nodeVariableMap, block, definedClass);

        injectionExpressionBuilder.buildVariable(injectionBuilderContext, injectionNode);

        Set<InjectionNode> proxied = new HashSet<InjectionNode>();
        for (InjectionNode node : nodeVariableMap.keySet()) {
            if(node.containsAspect(LateInit.class)){
                proxied.add(node);
            }
        }

        for (InjectionNode node : proxied) {
            injectionExpressionBuilder.setupInjectionRequirements(injectionBuilderContext, node);

            TypedExpression proxyExpression = node.getAspect(LateInit.class).getProxyVariable();
            node.getAspects().remove(VirtualProxyAspect.class);
            nodeVariableMap.remove(node);

            TypedExpression delegateVariable = injectionExpressionBuilder.buildVariable(injectionBuilderContext, node);

            //init proxy
            virtualProxyGenerator.initializeProxy(injectionBuilderContext, proxyExpression, delegateVariable);
        }

        return nodeVariableMap;
    }
}
