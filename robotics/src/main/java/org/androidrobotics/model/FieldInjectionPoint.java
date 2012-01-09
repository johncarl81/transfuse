package org.androidrobotics.model;

import org.androidrobotics.analysis.adapter.ASTAccessModifier;

/**
 * @author John Ericksen
 */
public class FieldInjectionPoint {

    private InjectionNode injectionNode;
    private String name;
    private ASTAccessModifier modifier;
    private boolean proxied;

    public FieldInjectionPoint(ASTAccessModifier modifier, String name, InjectionNode injectionNode) {
        this.modifier = modifier;
        this.injectionNode = injectionNode;
        this.name = name;
        this.proxied = false;
    }

    public String getName() {
        return name;
    }

    public InjectionNode getInjectionNode() {
        return injectionNode;
    }

    public boolean isProxied() {
        return proxied;
    }

    public void setProxied(boolean proxied) {
        this.proxied = proxied;
    }

    public ASTAccessModifier getAccessModifier() {
        return modifier;
    }
}
