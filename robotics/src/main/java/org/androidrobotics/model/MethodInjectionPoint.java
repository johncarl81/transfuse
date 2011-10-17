package org.androidrobotics.model;

/**
 * @author John Ericksen
 */
public class MethodInjectionPoint extends InjectionPointBase {

    private String name;

    public MethodInjectionPoint(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
