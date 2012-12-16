/**
 * Copyright 2012 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

    private final Set<ConstructorInjectionPoint> constructorInjectionPoints = new HashSet<ConstructorInjectionPoint>();
    private final Set<MethodInjectionPoint> methodInjectionPoints = new HashSet<MethodInjectionPoint>();
    private final Set<FieldInjectionPoint> fieldInjectionPoints = new HashSet<FieldInjectionPoint>();
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
