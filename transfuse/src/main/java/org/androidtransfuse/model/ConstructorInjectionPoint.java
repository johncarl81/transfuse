package org.androidtransfuse.model;

import org.androidtransfuse.analysis.adapter.ASTAccessModifier;

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
