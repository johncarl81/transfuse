/**
 * Copyright 2011-2015 John Ericksen
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

import org.androidtransfuse.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * InjectionNode aspect containing the relationship injection points for constructors, methods and fields.  These
 * are used during injection code generation, specifying what to inject into the associated InjectionNode.
 *
 * @author John Ericksen
 */
public class ASTInjectionAspect implements Aspect {

    public enum InjectionAssignmentType {
        FIELD,
        LOCAL
    }

    public static class InjectionGroup{

        private final List<MethodInjectionPoint> methodInjectionPoints = new ArrayList<MethodInjectionPoint>();
        private final List<FieldInjectionPoint> fieldInjectionPoints = new ArrayList<FieldInjectionPoint>();

        public InjectionGroup add(MethodInjectionPoint methodInjectionPoint) {
            methodInjectionPoints.add(0, methodInjectionPoint);
            return this;
        }

        public InjectionGroup add(FieldInjectionPoint fieldInjectionPoint) {
            fieldInjectionPoints.add(0, fieldInjectionPoint);
            return this;
        }

        public List<MethodInjectionPoint> getMethodInjectionPoints() {
            return methodInjectionPoints;
        }

        public List<FieldInjectionPoint> getFieldInjectionPoints() {
            return fieldInjectionPoints;
        }
    }

    private ConstructorInjectionPoint constructorInjectionPoint;
    private final List<InjectionGroup> groups = new ArrayList<InjectionGroup>();
    private InjectionAssignmentType assignmentType = InjectionAssignmentType.LOCAL;

    public void set(ConstructorInjectionPoint constructorInjectionPoint) {
        this.constructorInjectionPoint = constructorInjectionPoint;
    }

    public InjectionGroup addGroup(){
        groups.add(0, new InjectionGroup());
        return getCurrentGroup();
    }

    public InjectionGroup getCurrentGroup(){
        return groups.get(0);
    }

    public List<InjectionGroup> getGroups() {
        return groups;
    }

    public ConstructorInjectionPoint getConstructorInjectionPoint() {
        return constructorInjectionPoint;
    }

    public void addAllInjectionGroups(List<InjectionGroup> groups){
        this.groups.addAll(groups);
    }

    public InjectionAssignmentType getAssignmentType() {
        return assignmentType;
    }

    public void setAssignmentType(InjectionAssignmentType assignmentType) {
        this.assignmentType = assignmentType;
    }

    @Override
    public void log(InjectionNodeLogger logger) {
        logger.append("InjectionAspect{");
        logger.pushIndent();
        if(constructorInjectionPoint != null){
            logger.append("\n");
            constructorInjectionPoint.log(logger);
        }
        for (InjectionGroup group : groups) {
            for (FieldInjectionPoint fieldInjectionPoint : group.getFieldInjectionPoints()) {
                logger.append("\n");
                fieldInjectionPoint.log(logger);
            }
            for (MethodInjectionPoint metodInjectionPoint : group.getMethodInjectionPoints()) {
                logger.append("\n");
                metodInjectionPoint.log(logger);
            }
        }
        logger.popIndent();
        logger.append("\n}");
    }
}
