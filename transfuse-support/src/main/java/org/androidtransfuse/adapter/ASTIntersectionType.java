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
package org.androidtransfuse.adapter;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @author John Ericksen
 */
public class ASTIntersectionType implements ASTType {

    private List<ASTType> intersection;

    public ASTIntersectionType(List<ASTType> intersection){
        this.intersection = intersection;
    }

    @Override
    public ASTAccessModifier getAccessModifier() {
        return ASTAccessModifier.PUBLIC;
    }

    @Override
    public ImmutableSet<ASTMethod> getMethods() {
        return ImmutableSet.of();
    }

    @Override
    public ImmutableSet<ASTField> getFields() {
        return ImmutableSet.of();
    }

    @Override
    public ImmutableSet<ASTConstructor> getConstructors() {
        return ImmutableSet.of();
    }

    @Override
    public boolean isConcreteClass() {
        return false;
    }

    @Override
    public boolean isInterface() {
        return false;
    }

    @Override
    public boolean isFinal() {
        return false;
    }

    @Override
    public boolean isStatic() {
        return false;
    }

    @Override
    public boolean isInnerClass() {
        return false;
    }

    @Override
    public boolean isEnum() {
        return false;
    }

    @Override
    public ASTType getSuperClass() {
        return null;
    }

    @Override
    public ImmutableSet<ASTType> getInterfaces() {
        return ImmutableSet.of();
    }

    @Override
    public ImmutableList<ASTType> getGenericParameters() {
        return ImmutableList.of();
    }

    @Override
    public ImmutableSet<ASTType> getInnerTypes() {
        return ImmutableSet.of();
    }

    @Override
    public boolean inheritsFrom(ASTType type) {
        for (ASTType astType : intersection) {
            if(astType.inheritsFrom(type)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean extendsFrom(ASTType type) {
        for (ASTType astType : intersection) {
            if(astType.extendsFrom(type)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean implementsFrom(ASTType type) {
        for (ASTType astType : intersection) {
            if(astType.implementsFrom(type)){
                return true;
            }
        }
        return false;
    }

    @Override
    public PackageClass getPackageClass() {
        return intersection.iterator().next().getPackageClass();
    }

    @Override
    public boolean isAnnotated(Class<? extends Annotation> annotation) {
        return false;
    }

    @Override
    public ImmutableSet<ASTAnnotation> getAnnotations() {
        return ImmutableSet.of();
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotation) {
        return null;
    }

    @Override
    public ASTAnnotation getASTAnnotation(Class<? extends Annotation> annotation) {
        return null;
    }

    @Override
    public String getName() {
        return Joiner.on("&").join(intersection);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ASTIntersectionType)) {
            return false;
        }

        ASTIntersectionType that = (ASTIntersectionType) o;
        return new EqualsBuilder().append(intersection, that.intersection).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(intersection).hashCode();
    }
}
