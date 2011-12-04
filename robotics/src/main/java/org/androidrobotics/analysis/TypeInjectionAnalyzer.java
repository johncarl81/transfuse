package org.androidrobotics.analysis;

import org.androidrobotics.analysis.adapter.ASTConstructor;
import org.androidrobotics.analysis.adapter.ASTField;
import org.androidrobotics.analysis.adapter.ASTMethod;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.gen.VariableBuilder;
import org.androidrobotics.gen.VariableBuilderRepository;
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
    public InjectionNode analyze(ASTType astType, VariableBuilderRepository variableBuilderRepository) {
        AnalysisContext analysisContext = new AnalysisContext(variableBuilderRepository);

        return analyze(astType, analysisContext, false);
    }

    protected InjectionNode analyze(final ASTType astType, final AnalysisContext context, final boolean proxyDependencyPossible) {
        VariableBuilder variableBuilder = context.getVariableBuilderRepository().get(astType.getName());

        return variableBuilder.processInjectionNode(astType, new AnalysisDependencyProcessingCallback() {
            public InjectionNode processInjectionNode(ASTType resolvedASTType) {
                InjectionNode node = new InjectionNode(resolvedASTType);

                if (context.isDependent(resolvedASTType)) {
                    //if this type is a dependency of itself, we've found a back link.
                    //This injection must be performed using a delayed injection proxy
                    InjectionNode injectionNode = context.getInjectionNode(resolvedASTType);

                    //if its a proxy dependency then the given dependent object will have to be build using a delayed
                    //proxy object
                    if (proxyDependencyPossible) {
                        injectionNode.setProxyRequired(true);
                        injectionNode.addProxyInterface(astType);
                    }

                    return injectionNode;
                }

                AnalysisContext nextContext = context.addDependent(resolvedASTType, node);

                ASTConstructor noArgConstructor = null;
                boolean constructorFound = false;

                for (ASTConstructor astConstructor : resolvedASTType.getConstructors()) {
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

                for (ASTMethod astMethod : resolvedASTType.getMethods()) {
                    if (astMethod.isAnnotated(Inject.class)) {
                        node.addInjectionPoint(injectionPointFactory.buildInjectionPoint(astMethod, nextContext));
                    }
                }

                for (ASTField astField : resolvedASTType.getFields()) {
                    if (astField.isAnnotated(Inject.class)) {
                        node.addInjectionPoint(injectionPointFactory.buildInjectionPoint(astField, nextContext));
                    }
                }

                return node;
            }
        });
    }
}
