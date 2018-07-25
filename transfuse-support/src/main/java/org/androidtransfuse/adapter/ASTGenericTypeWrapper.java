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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.inject.Inject;
import java.lang.annotation.Annotation;

/**
 * @author John Ericksen
 */
public class ASTGenericTypeWrapper implements ASTType {

    private final ASTType astType;
    private final LazyTypeParameterBuilder lazyTypeParameterBuilder;

    @Inject
    public ASTGenericTypeWrapper(/*@Assisted*/ ASTType astType, /*@Assisted*/ LazyTypeParameterBuilder lazyTypeParameterBuilder) {
        this.astType = astType;
        this.lazyTypeParameterBuilder = lazyTypeParameterBuilder;
    }

    @Override
    public ImmutableList<ASTType> getGenericArgumentTypes() {
        return lazyTypeParameterBuilder.buildGenericParameters();
    }

    @Override
    public ImmutableList<ASTGenericArgument> getGenericArguments() {
        return astType.getGenericArguments();
    }

    public ASTType getWrappedType(){
        return astType;
    }

    @Override
    public ASTAccessModifier getAccessModifier() {
        return astType.getAccessModifier();
    }

    public ImmutableSet<ASTMethod> getMethods() {
        return astType.getMethods();
    }

    public ImmutableSet<ASTField> getFields() {
        return astType.getFields();
    }

    public ImmutableSet<ASTConstructor> getConstructors() {
        return astType.getConstructors();
    }

    public boolean isConcreteClass() {
        return astType.isConcreteClass();
    }

    public boolean isInterface() {
        return astType.isInterface();
    }

    @Override
    public boolean isEnum() {
        return astType.isEnum();
    }

    @Override
    public boolean isFinal() {
        return astType.isFinal();
    }

    @Override
    public boolean isStatic() {
        return astType.isStatic();
    }

    @Override
    public boolean isAbstract() {
        return astType.isAbstract();
    }

    @Override
    public boolean isInnerClass() {
        return astType.isInnerClass();
    }

    public ASTType getSuperClass() {
        return astType.getSuperClass();
    }

    public ImmutableSet<ASTType> getInterfaces() {
        return astType.getInterfaces();
    }

    public boolean isAnnotated(Class<? extends Annotation> annotation) {
        return astType.isAnnotated(annotation);
    }

    @Override
    public boolean isAnnotated(ASTType annotation) {
        return ASTUtils.getInstance().isAnnotated(annotation, getAnnotations());
    }

    public ImmutableSet<ASTAnnotation> getAnnotations() {
        return astType.getAnnotations();
    }

    public <A extends Annotation> A getAnnotation(Class<A> annotation) {
        return astType.getAnnotation(annotation);
    }

    @Override
    public ImmutableSet<ASTType> getInnerTypes() {
        return astType.getInnerTypes();
    }

    public String getName() {
        return astType.getName();
    }

    @Override
    public boolean inherits(ASTType type) {
        return astType.inherits(type);
    }

    @Override
    public ASTAnnotation getASTAnnotation(Class<? extends Annotation> annotation) {
        return astType.getASTAnnotation(annotation);
    }

    @Override
    public PackageClass getPackageClass() {
        return astType.getPackageClass();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ASTType)) {
            return false;
        }

        ASTType that = (ASTType) o;

        return new EqualsBuilder()
                .append(getName(), that.getName())
                .append(getGenericArgumentTypes(), that.getGenericArgumentTypes())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getName()).hashCode();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append('<');
        builder.append(StringUtils.join(getGenericArgumentTypes(), ", "));
        builder.append('>');

        return getName() + builder.toString();
    }
}
