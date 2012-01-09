package org.androidrobotics.model;

import org.androidrobotics.analysis.adapter.ASTAccessModifier;

/**
 * @author John Ericksen
 */
public class MethodInjectionPoint extends InjectionPointBase {

    private String name;
    private ASTAccessModifier accessModifier;

    public MethodInjectionPoint(ASTAccessModifier accessModifier, String name) {
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
