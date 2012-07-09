package org.androidtransfuse.model;

import org.androidtransfuse.analysis.adapter.ASTAccessModifier;
import org.androidtransfuse.analysis.adapter.ASTType;

/**
 * @author John Ericksen
 */
public class FieldInjectionPoint {

    private ASTType containingType;
    private InjectionNode injectionNode;
    private String name;
    private ASTAccessModifier modifier;

    public FieldInjectionPoint(ASTType containingType, ASTAccessModifier modifier, String name, InjectionNode injectionNode) {
        this.modifier = modifier;
        this.injectionNode = injectionNode;
        this.name = name;
        this.containingType = containingType;
    }

    public String getName() {
        return name;
    }

    public InjectionNode getInjectionNode() {
        return injectionNode;
    }

    public ASTAccessModifier getAccessModifier() {
        return modifier;
    }

    public ASTType getContainingType() {
        return containingType;
    }
}
