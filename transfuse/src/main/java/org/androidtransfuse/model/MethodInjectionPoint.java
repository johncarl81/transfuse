package org.androidtransfuse.model;

import org.androidtransfuse.analysis.adapter.ASTAccessModifier;
import org.androidtransfuse.analysis.adapter.ASTType;

/**
 * @author John Ericksen
 */
public class MethodInjectionPoint extends MethodInjectionPointBase {

    private final String name;
    private final ASTAccessModifier accessModifier;

    public MethodInjectionPoint(ASTType containingType, ASTAccessModifier accessModifier, String name) {
        super(containingType);
        this.name = name;
        this.accessModifier = accessModifier;
    }

    public String getName() {
        return name;
    }

    public ASTAccessModifier getAccessModifier() {
        return accessModifier;
    }
}
