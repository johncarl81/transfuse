package org.androidtransfuse.model;

import org.androidtransfuse.analysis.adapter.ASTAccessModifier;

/**
 * @author John Ericksen
 */
public class FieldInjectionPoint {

    private InjectionNode injectionNode;
    private String name;
    private ASTAccessModifier modifier;
    private boolean proxied;
    private int subclassLevel;

    public FieldInjectionPoint(ASTAccessModifier modifier, String name, InjectionNode injectionNode, int subclassLevel) {
        this.modifier = modifier;
        this.injectionNode = injectionNode;
        this.name = name;
        this.subclassLevel = subclassLevel;
        this.proxied = false;
    }

    public String getName() {
        return name;
    }

    public InjectionNode getInjectionNode() {
        return injectionNode;
    }

    public void setProxied(boolean proxied) {
        this.proxied = proxied;
    }

    public ASTAccessModifier getAccessModifier() {
        return modifier;
    }

    public int getSubclassLevel() {
        return proxied ? subclassLevel + 1 : subclassLevel;
    }
}
