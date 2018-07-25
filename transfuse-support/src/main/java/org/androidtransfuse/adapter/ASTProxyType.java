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
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.lang.annotation.Annotation;


/**
 * Replaces the given AST Type with a proxy type.  This is used during AOP and Virtual proxy of the given
 * ASTType.  Simply replaces the name of the class with the proxy name.
 *
 * @author John Ericksen
 */
public class ASTProxyType implements ASTType {

    private final ASTType proxyASTType;
    private final String name;

    public ASTProxyType(ASTType proxyASTType, String name) {
        this.proxyASTType = proxyASTType;
        this.name = name;
    }

    @Override
    public boolean isConcreteClass() {
        return proxyASTType.isConcreteClass();
    }

    public boolean isInterface() {
        return proxyASTType.isInterface();
    }

    @Override
    public boolean isFinal() {
        return proxyASTType.isFinal();
    }

    @Override
    public boolean isEnum() {
        return proxyASTType.isEnum();
    }

    @Override
    public boolean isStatic() {
        return proxyASTType.isStatic();
    }

    @Override
    public boolean isAbstract() {
        return proxyASTType.isAbstract();
    }

    @Override
    public boolean isInnerClass() {
        return proxyASTType.isInnerClass();
    }

    @Override
    public ImmutableSet<ASTType> getInnerTypes() {
        return proxyASTType.getInnerTypes();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ASTAccessModifier getAccessModifier() {
        return proxyASTType.getAccessModifier();
    }

    @Override
    public ImmutableSet<ASTMethod> getMethods() {
        return proxyASTType.getMethods();
    }

    @Override
    public ImmutableSet<ASTField> getFields() {
        return proxyASTType.getFields();
    }

    @Override
    public boolean isAnnotated(Class<? extends Annotation> annotation) {
        return proxyASTType.isAnnotated(annotation);
    }

    @Override
    public ImmutableSet<ASTAnnotation> getAnnotations() {
        return proxyASTType.getAnnotations();
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotation) {
        return proxyASTType.getAnnotation(annotation);
    }

    @Override
    public boolean isAnnotated(ASTType annotation) {
        return proxyASTType.isAnnotated(annotation);
    }

    @Override
    public ImmutableSet<ASTConstructor> getConstructors() {
        return proxyASTType.getConstructors();
    }

    @Override
    public ASTType getSuperClass() {
        return proxyASTType.getSuperClass();
    }

    @Override
    public ImmutableSet<ASTType> getInterfaces() {
        return proxyASTType.getInterfaces();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ASTProxyType)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        ASTProxyType rhs = (ASTProxyType) obj;
        return new EqualsBuilder()
                .append(name, rhs.name)
                .append(proxyASTType, rhs.proxyASTType)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).append(proxyASTType).hashCode();
    }

    @Override
    public ImmutableList<ASTType> getGenericArgumentTypes() {
        return proxyASTType.getGenericArgumentTypes();
    }

    @Override
    public ImmutableList<ASTGenericArgument> getGenericArguments() {
        return proxyASTType.getGenericArguments();
    }

    @Override
    public boolean inherits(ASTType type) {
        return proxyASTType.inherits(type);
    }

    @Override
    public ASTAnnotation getASTAnnotation(Class<? extends Annotation> annotation) {
        return proxyASTType.getASTAnnotation(annotation);
    }

    @Override
    public PackageClass getPackageClass() {
        return proxyASTType.getPackageClass();
    }

    @Override
    public String toString() {
        return getName();
    }
}
