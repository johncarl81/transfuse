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
        InjectionNode node = new InjectionNode(astType.getName());

        ASTConstructor noArgConstructor = null;
        boolean constructorFound = false;

        for (ASTConstructor astConstructor : astType.getConstructors()) {
            if (astConstructor.isAnnotated(Inject.class)) {
                node.addInjectionPoint(injectionPointFactory.buildInjectionPoint(astConstructor));
                constructorFound = true;
            }
            if (astConstructor.getParameters().size() == 0) {
                noArgConstructor = astConstructor;
            }
        }

        if (!constructorFound) {
            node.addInjectionPoint(injectionPointFactory.buildInjectionPoint(noArgConstructor));
        }

        for (ASTMethod astMethod : astType.getMethods()) {
            if (astMethod.isAnnotated(Inject.class)) {
                node.addInjectionPoint(injectionPointFactory.buildInjectionPoint(astMethod));
            }
        }

        for (ASTField astField : astType.getFields()) {
            if (astField.isAnnotated(Inject.class)) {
                node.addInjectionPoint(injectionPointFactory.buildInjectionPoint(astField));
            }
        }

        return node;
    }
}
