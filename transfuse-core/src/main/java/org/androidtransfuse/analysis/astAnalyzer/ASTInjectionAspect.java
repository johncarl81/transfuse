/**
 * Copyright 2013 John Ericksen
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

import java.util.ArrayList;
import java.util.List;

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

    private final List<ConstructorInjectionPoint> constructorInjectionPoints = new ArrayList<ConstructorInjectionPoint>();
    private final List<MethodInjectionPoint> methodInjectionPoints = new ArrayList<MethodInjectionPoint>();
    private final List<FieldInjectionPoint> fieldInjectionPoints = new ArrayList<FieldInjectionPoint>();
    private InjectionAssignmentType assignmentType = InjectionAssignmentType.LOCAL;

    public void add(ConstructorInjectionPoint constructorInjectionPoint) {
        constructorInjectionPoints.add(0, constructorInjectionPoint);
    }

    public void add(MethodInjectionPoint methodInjectionPoint) {
        methodInjectionPoints.add(0, methodInjectionPoint);
    }

    public void add(FieldInjectionPoint fieldInjectionPoint) {
        fieldInjectionPoints.add(0, fieldInjectionPoint);
    }

    public ConstructorInjectionPoint getConstructorInjectionPoint() {
        return constructorInjectionPoints.iterator().next();
    }

    public List<ConstructorInjectionPoint> getConstructorInjectionPoints() {
        return constructorInjectionPoints;
    }

    public List<MethodInjectionPoint> getMethodInjectionPoints() {
        return methodInjectionPoints;
    }

    public List<FieldInjectionPoint> getFieldInjectionPoints() {
        return fieldInjectionPoints;
    }

    public void addAllFieldInjectionPoints(List<FieldInjectionPoint> fieldInjectionPoints) {
        this.fieldInjectionPoints.addAll(fieldInjectionPoints);
    }

    public void addAllMethodInjectionPoints(List<MethodInjectionPoint> methodInjectionPoints) {
        this.methodInjectionPoints.addAll(methodInjectionPoints);
    }

    public InjectionAssignmentType getAssignmentType() {
        return assignmentType;
    }

    public void setAssignmentType(InjectionAssignmentType assignmentType) {
        this.assignmentType = assignmentType;
    }
}
