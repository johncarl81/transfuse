package org.androidrobotics.model;

import org.androidrobotics.analysis.adapter.ASTAccessModifier;

/**
 * @author John Ericksen
 */
public class MethodInjectionPoint extends InjectionPointBase {

    private String name;
    private ASTAccessModifier accessModifier;
    private int superClassLevel;
    private boolean proxied;

    public MethodInjectionPoint(ASTAccessModifier accessModifier, String name, int superClassLevel) {
        this.name = name;
        this.accessModifier = accessModifier;
        this.superClassLevel = superClassLevel;
    }

    public String getName() {
        return name;
    }

    public ASTAccessModifier getAccessModifier() {
        return accessModifier;
    }

    public int getSuperClassLevel() {
        return proxied ? superClassLevel + 1 : superClassLevel;
    }

    public void setProxied(boolean proxied) {
        this.proxied = proxied;
    }
}
