package org.androidrobotics.analysis;

import org.androidrobotics.analysis.adapter.ASTConstructor;
import org.androidrobotics.analysis.adapter.ASTField;
import org.androidrobotics.analysis.adapter.ASTMethod;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.model.InjectionNode;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Analysis class for ASTType Injection Analysis
 *
 * @author John Ericksen
 */
@Singleton
public class TypeInjectionAnalyzer {

    @Inject
    private InjectionPointFactory injectionPointFactory;

    /**
     * Analyze the given ASTType and produces a corresponding InjectionNode with the contained
     * AST injection related elements (constructor, method, field) recursively analyzed for InjectionNodes
     *
     * @param astType required type
     * @return InjectionNode root
     */
    public InjectionNode analyze(ASTType astType) {
        AnalysisContext analysisContext = new AnalysisContext();

        return analyze(astType, analysisContext, false);
    }

    protected InjectionNode analyze(ASTType astType, AnalysisContext context, boolean proxyDependencyPossible) {
        InjectionNode node = new InjectionNode(astType.getName());

        if (context.isDependent(astType)) {
            //if this type is a dependency of itself, we've found a back link.
            //This injection must be performed using a delayed injection proxy
            InjectionNode injectionNode = context.getInjectionNode(astType);

            //if its a proxy dependency then the given dependent object will have to be build using a delayed
            //proxy object
            if (proxyDependencyPossible) {
                injectionNode.setProxyRequired(true);
            }

            return injectionNode;
        }

        AnalysisContext nextContext = context.addDependent(astType, node);

        ASTConstructor noArgConstructor = null;
        boolean constructorFound = false;

        for (ASTConstructor astConstructor : astType.getConstructors()) {
            if (astConstructor.isAnnotated(Inject.class)) {
                node.addInjectionPoint(injectionPointFactory.buildInjectionPoint(astConstructor, nextContext));
                constructorFound = true;
            }
            if (astConstructor.getParameters().size() == 0) {
                noArgConstructor = astConstructor;
            }
        }

        if (!constructorFound) {
            if (noArgConstructor == null) {
                throw new RoboticsAnalysisException("No-Arg Constructor required for injection point: " + node.getClassName());
            }
            node.addInjectionPoint(injectionPointFactory.buildInjectionPoint(noArgConstructor, nextContext));
        }

        for (ASTMethod astMethod : astType.getMethods()) {
            if (astMethod.isAnnotated(Inject.class)) {
                node.addInjectionPoint(injectionPointFactory.buildInjectionPoint(astMethod, nextContext));
            }
        }

        for (ASTField astField : astType.getFields()) {
            if (astField.isAnnotated(Inject.class)) {
                node.addInjectionPoint(injectionPointFactory.buildInjectionPoint(astField, nextContext));
            }
        }

        return node;
    }
}
