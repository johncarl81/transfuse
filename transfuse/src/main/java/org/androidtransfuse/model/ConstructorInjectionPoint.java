package org.androidtransfuse.model;

import org.androidtransfuse.analysis.adapter.ASTAccessModifier;
import org.androidtransfuse.analysis.adapter.ASTType;

/**
 * @author John Ericksen
 */
public class ConstructorInjectionPoint extends MethodInjectionPointBase {

    private final ASTAccessModifier modifier;

    public ConstructorInjectionPoint(ASTAccessModifier modifier, ASTType containingType) {
        super(containingType);
        this.modifier = modifier;
    }

    public ASTAccessModifier getAccessModifier() {
        return modifier;
    }
}
