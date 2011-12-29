package org.androidrobotics.model;

/**
 * @author John Ericksen
 */
public class FieldInjectionPoint {

    private InjectionNode injectionNode;
    private String name;
    private boolean proxied;

    public FieldInjectionPoint(String name, InjectionNode injectionNode) {
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
}
