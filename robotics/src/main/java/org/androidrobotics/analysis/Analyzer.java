package org.androidrobotics.analysis;

import org.androidrobotics.analysis.adapter.ASTField;
import org.androidrobotics.analysis.adapter.ASTMethod;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.analysis.astAnalyzer.VirtualProxyAspect;
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

            VirtualProxyAspect proxyAspect = getProxyAspect(injectionNode);
            proxyAspect.getProxyInterfaces().add(instanceType);

        } else {
            injectionNode = new InjectionNode(instanceType, concreteType);
            //default variable builder
            injectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderProvider.get());

            AnalysisContext nextContext = context.addDependent(concreteType, injectionNode);

            //loop over super classes (extension and implements
            scanClassHierarchy(concreteType, injectionNode, nextContext);
        }

        return injectionNode;
    }

    private void scanClassHierarchy(ASTType concreteType, InjectionNode injectionNode, AnalysisContext context) {
        if (concreteType.getSuperClass() != null) {
            scanClassHierarchy(concreteType.getSuperClass(), injectionNode, context.incrementSuperClassLevel());
        }

        for (ASTAnalysis analysis : context.getAnalysisRepository().getAnalysisSet()) {

            analysis.analyzeType(injectionNode, concreteType, context);

            for (ASTMethod astMethod : concreteType.getMethods()) {
                analysis.analyzeMethod(injectionNode, astMethod, context);
            }

            for (ASTField astField : concreteType.getFields()) {
                analysis.analyzeField(injectionNode, astField, context);
            }
        }
    }

    private VirtualProxyAspect getProxyAspect(InjectionNode injectionNode) {
        if (!injectionNode.containsAspect(VirtualProxyAspect.class)) {
            injectionNode.addAspect(new VirtualProxyAspect());
        }
        return injectionNode.getAspect(VirtualProxyAspect.class);
    }
}
