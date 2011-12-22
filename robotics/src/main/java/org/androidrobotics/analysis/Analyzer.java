package org.androidrobotics.analysis;

import org.androidrobotics.analysis.adapter.ASTField;
import org.androidrobotics.analysis.adapter.ASTMethod;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.analysis.astAnalyzer.ProxyAspect;
import org.androidrobotics.gen.variableBuilder.VariableBuilder;
import org.androidrobotics.gen.variableBuilder.VariableInjectionBuilder;
import org.androidrobotics.model.InjectionNode;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Analysis class for ASTType Injection Analysis
 *
 * @author John Ericksen
 */
public class Analyzer {

    @Inject
    private Provider<VariableInjectionBuilder> variableInjectionBuilderProvider;

    /**
     * Analyze the given ASTType and produces a corresponding InjectionNode with the contained
     * AST injection related elements (constructor, method, field) recursively analyzed for InjectionNodes
     *
     * @param concreteType required type
     * @return InjectionNode root
     */
    public InjectionNode analyze(final ASTType instanceType, final ASTType concreteType, final AnalysisContext context) {

        InjectionNode injectionNode;

        if (context.isDependent(concreteType)) {
            //if this type is a dependency of itself, we've found a back link.
            //This injection must be performed using a delayed injection proxy
            injectionNode = context.getInjectionNode(concreteType);

            ProxyAspect proxyAspect = getProxyAspect(injectionNode);
            proxyAspect.setProxyRequired(true);
            proxyAspect.getProxyInterfaces().add(instanceType);

        } else {
            injectionNode = new InjectionNode(concreteType);
            //default variable builder
            injectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderProvider.get());

            AnalysisContext nextContext = context.addDependent(concreteType, injectionNode);

            for (ASTAnalysis analysis : context.getAnalysisRepository().getAnalysisSet()) {

                analysis.analyzeType(injectionNode, concreteType, nextContext);

                for (ASTMethod astMethod : concreteType.getMethods()) {
                    analysis.analyzeMethod(injectionNode, astMethod, nextContext);
                }

                for (ASTField astField : concreteType.getFields()) {
                    analysis.analyzeField(injectionNode, astField, nextContext);
                }
            }
        }

        return injectionNode;
    }

    private ProxyAspect getProxyAspect(InjectionNode injectionNode) {
        if (!injectionNode.containsAspect(ProxyAspect.class)) {
            injectionNode.addAspect(new ProxyAspect());
        }
        return injectionNode.getAspect(ProxyAspect.class);
    }
}
