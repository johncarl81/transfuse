package org.androidrobotics.analysis.astAnalyzer;

import org.androidrobotics.model.ConstructorInjectionPoint;
import org.androidrobotics.model.DependencyInjectionPoint;
import org.androidrobotics.model.FieldInjectionPoint;
import org.androidrobotics.model.MethodInjectionPoint;

import java.util.HashSet;
import java.util.Set;

/**
 * @author John Ericksen
 */
public class ASTInjectionAspect {

    private Set<ConstructorInjectionPoint> constructorInjectionPoints = new HashSet<ConstructorInjectionPoint>();
    private Set<MethodInjectionPoint> methodInjectionPoints = new HashSet<MethodInjectionPoint>();
    private Set<FieldInjectionPoint> fieldInjectionPoints = new HashSet<FieldInjectionPoint>();
    private Set<DependencyInjectionPoint> dependencyInjectionPoints = new HashSet<DependencyInjectionPoint>();

    public void add(ConstructorInjectionPoint constructorInjectionPoint) {
        constructorInjectionPoints.add(constructorInjectionPoint);
    }

    public void add(MethodInjectionPoint methodInjectionPoint) {
        methodInjectionPoints.add(methodInjectionPoint);
    }

    public void add(FieldInjectionPoint fieldInjectionPoint) {
        fieldInjectionPoints.add(fieldInjectionPoint);
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

    public void addAllFieldInjectionPoints(Set<FieldInjectionPoint> fieldInjectionPoints) {
        this.fieldInjectionPoints.addAll(fieldInjectionPoints);
    }

    public void addAllMethodInjectionPoints(Set<MethodInjectionPoint> methodInjectionPoints) {
        this.methodInjectionPoints.addAll(methodInjectionPoints);
    }

    public void addAllConstructorInjectionPoints(Set<ConstructorInjectionPoint> constructorInjectionPoints) {
        this.constructorInjectionPoints.addAll(constructorInjectionPoints);
    }

    public void add(DependencyInjectionPoint dependencyInjectionPoint) {
        this.dependencyInjectionPoints.add(dependencyInjectionPoint);
    }

    public Set<DependencyInjectionPoint> getDependencyInjectionPoints() {
        return dependencyInjectionPoints;
    }
}
