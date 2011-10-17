package org.androidrobotics.model;

/**
 * @author John Ericksen
 */
public class FieldInjectionPoint {

    private InjectionNode injectionNode;
    private String name;

    public FieldInjectionPoint(String name, InjectionNode injectionNode) {
        this.injectionNode = injectionNode;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public InjectionNode getInjectionNode() {
        return injectionNode;
    }
}
