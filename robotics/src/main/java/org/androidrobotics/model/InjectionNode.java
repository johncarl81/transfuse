package org.androidrobotics.model;

import java.util.HashSet;
import java.util.Set;

/**
 * @author John Ericksen
 */
public class InjectionNode {

    private String className;
    private boolean proxyRequired;

    private Set<ConstructorInjectionPoint> constructorInjectionPoints = new HashSet<ConstructorInjectionPoint>();
    private Set<MethodInjectionPoint> methodInjectionPoints = new HashSet<MethodInjectionPoint>();
    private Set<FieldInjectionPoint> fieldInjectionPoints = new HashSet<FieldInjectionPoint>();

    public InjectionNode(String className) {
        this.className = className;
    }

    public ConstructorInjectionPoint getConstructorInjectionPoint() {
        return constructorInjectionPoints.iterator().next();
    }

    public Set<ConstructorInjectionPoint> getConstructorInjectionPoints() {
        return constructorInjectionPoints;
    }

    public Set<MethodInjectionPoint> getMethodInjectionPoints() {
        return methodInjectionPoints;
    }

    public Set<FieldInjectionPoint> getFieldInjectionPoints() {
        return fieldInjectionPoints;
    }

    public void addInjectionPoint(ConstructorInjectionPoint constructorInjectionPoint) {
        constructorInjectionPoints.add(constructorInjectionPoint);
    }

    public void addInjectionPoint(MethodInjectionPoint methodInjectionPoint) {
        methodInjectionPoints.add(methodInjectionPoint);
    }

    public void addInjectionPoint(FieldInjectionPoint fieldInjectionPoint) {
        fieldInjectionPoints.add(fieldInjectionPoint);
    }

    public String getClassName() {
        return className;
    }

    public void setProxyRequired(boolean proxyRequired) {
        this.proxyRequired = proxyRequired;
    }

    public boolean isProxyRequired() {
        return proxyRequired;
    }
}
