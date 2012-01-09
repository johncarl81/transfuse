package org.androidrobotics.model;

import org.androidrobotics.analysis.adapter.ASTAccessModifier;

/**
 * @author John Ericksen
 */
public class ConstructorInjectionPoint extends InjectionPointBase {

    private ASTAccessModifier modifier;

    public ConstructorInjectionPoint(ASTAccessModifier modifier) {
        this.modifier = modifier;
    }

    public ASTAccessModifier getAccessModifier() {
        return modifier;
    }
}
