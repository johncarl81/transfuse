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
import java.util.Stack;

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
            //This dependency loop must be broken using a virtual proxy
            injectionNode = context.getInjectionNode(concreteType);

            Stack<InjectionNode> loopedDependencies = context.getDependencyHistory();

            InjectionNode proxyDependency = findProxyableDependency(injectionNode, loopedDependencies);

            if (proxyDependency == null) {
                throw new RoboticsAnalysisException("Unable to find a depenency to proxy");
            }

            VirtualProxyAspect proxyAspect = getProxyAspect(proxyDependency);
            proxyAspect.getProxyInterfaces().add(proxyDependency.getUsageType());

        } else {
            injectionNode = new InjectionNode(instanceType, concreteType);
            //default variable builder
            injectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderProvider.get());

            AnalysisContext nextContext = context.addDependent(injectionNode);

            //loop over super classes (extension and implements)
            scanClassHierarchy(concreteType, injectionNode, nextContext);
        }

        return injectionNode;
    }

    private InjectionNode findProxyableDependency(InjectionNode duplicateDepedency, Stack<InjectionNode> loopedDependencies) {
        if (!duplicateDepedency.getUsageType().isConcreteClass()) {
            return duplicateDepedency;
        }
        for (InjectionNode loopInjectionNode = loopedDependencies.pop(); !loopedDependencies.empty() && loopInjectionNode != duplicateDepedency; loopInjectionNode = loopedDependencies.pop()) {
            if (!loopInjectionNode.getUsageType().isConcreteClass()) {
                //found interface
                return loopInjectionNode;
            }
        }

        return null;
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
