package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.model.ConstructorInjectionPoint;
import org.androidtransfuse.model.FieldInjectionPoint;
import org.androidtransfuse.model.MethodInjectionPoint;

import java.util.HashSet;
import java.util.Set;

/**
 * InjectionNode aspect containing the relationship injection points for constructors, methods and fields.  These
 * are used during injection code generation, specifying what to inject into the associated InjectionNode.
 *
 * @author John Ericksen
 */
public class ASTInjectionAspect {

    public enum InjectionAssignmentType {
        FIELD,
        LOCAL
    }

    private Set<ConstructorInjectionPoint> constructorInjectionPoints = new HashSet<ConstructorInjectionPoint>();
    private Set<MethodInjectionPoint> methodInjectionPoints = new HashSet<MethodInjectionPoint>();
    private Set<FieldInjectionPoint> fieldInjectionPoints = new HashSet<FieldInjectionPoint>();
    private InjectionAssignmentType assignmentType = InjectionAssignmentType.LOCAL;

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

    public InjectionAssignmentType getAssignmentType() {
        return assignmentType;
    }

    public void setAssignmentType(InjectionAssignmentType assignmentType) {
        this.assignmentType = assignmentType;
    }
}
